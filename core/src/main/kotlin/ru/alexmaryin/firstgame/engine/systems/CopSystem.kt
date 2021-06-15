package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.entities.Cop
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class CopSystem : IteratingSystem(
    allOf(CopComponent::class, TransformComponent::class, MoveComponent::class)
        .exclude(RemoveComponent::class).get()
), EntityListener {

    inner class CopPool : Pool<Cop>() {
        override fun newObject() = Cop(engine).also {
            log.debug { "New cop created ${it.hashCode()}" }
        }
    }

    private val log = logger<CopSystem>()

    private val newPosition = Vector3()
    private val activeCops = GdxArray<Cop>()
    private val copsPool = CopPool()
    private val enemies by lazy { engine.getEntitiesFor(allOf(EnemyComponent::class).exclude(RemoveComponent::class).get()) }
    private val players by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()) }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        log.debug { "Cop added to engine ${entity.hashCode()}" }
        val transform = entity.transform
        val animation = entity.animation

        transform.offset.set(Cop.X_SPRITE_OFFSET, Cop.Y_SPRITE_OFFSET)
        transform.size.set(Cop.WIDTH_SPRITE_RATIO, Cop.HEIGHT_SPRITE_RATIO)
        transform.setInitialPosition(newPosition.x, newPosition.y, newPosition.z)
        animation.type = AnimationType.COP_WALK_FROM_LEFT
    }

    override fun entityRemoved(entity: Entity?) {
        Gdx.input.vibrate(100)
        activeCops.removeValue(entity as Cop, false)
        copsPool.free(entity)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val cop = entity.cop

        // At first, let's make next move
        when (cop.state) {
            CopState.WALK_TO_ENEMY -> entity.move.moveToPosition(Move.SlowLeft)
            CopState.WALK_BACK -> entity.move.moveToPosition(Move.SlowRight)
            CopState.ATTACK -> {
                entity.animation.type = AnimationType.COP_ATTACK
                entity.facing?.direction = FacingDirection.LEFT
                cop.attackTime -= deltaTime
                if (cop.attackTime <= 0) {
                    cop.state = CopState.WALK_BACK
                    entity.animation.type = AnimationType.COP_WALK_FROM_LEFT
                    engine.getSystem<DamageSystem>().addCaughtEnemy()
                }
                return          // No need check other states if cop is attacking
            }
        }

        // Check whether cop overlaps any of enemies with state walk_straight
        val boundingRect = Rectangle(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
        enemies.forEach { enemyComp ->
            with(enemyComp.transform) {
                val enemyBound = Rectangle(position.x, position.y, size.x, size.y)
                if (boundingRect.overlaps(enemyBound) && enemyComp.enemy.state == EnemyState.WALK_STRAIGHT) {
                    cop.state = CopState.ATTACK
                    cop.attackTime = random(Gameplay.ENEMY_ATTACK_MIN_INTERVAL, Gameplay.ENEMY_ATTACK_MAX_INTERVAL)
                    enemyComp.enemy.underAttackTime = cop.attackTime
                    enemyComp.enemy.state = EnemyState.UNDER_ATTACK
                    return
                }
            }
        }

        // Check whether cop overlaps player
        players.forEach { playerComp ->
            with(playerComp.transform) {
                val playerBound = Rectangle(position.x, position.y, size.x, size.y)
                if (boundingRect.overlaps(playerBound) && cop.state == CopState.WALK_BACK) {
                    playerComp.player.restoreCop()
                    removeCopFromScreen(entity)
                    return
                }
            }
        }

        // At the end, check whether cop has reached the screen border and is going to disappear
        if (transform.position.x <= 0f || transform.position.x >= WorldDimens.F_WIDTH - 1) {
            cop.isMissed = true
            removeCopFromScreen(entity)
        }
    }

    fun addCop(position: Vector2) {
        log.debug { "Add cop invoked with pos $position" }
        newPosition.set(position, 1f)
        val newCop = copsPool.obtain()
        newCop.remove<RemoveComponent>()
        engine.addEntity(newCop)
        activeCops.add(newCop)
    }

    private fun removeCopFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
        if (entity.cop.isMissed) engine.getSystem<DamageSystem>().addMissedCop()
    }
}