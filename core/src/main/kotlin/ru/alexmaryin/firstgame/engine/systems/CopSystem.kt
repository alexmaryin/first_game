package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ktx.collections.GdxArray
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
) {

    inner class CopPool : Pool<Cop>() {
        override fun newObject() = Cop(engine)
    }

    private val log = logger<CopSystem>()

    private val newPosition = Vector3()
    private val activeCops = GdxArray<Cop>()
    private val copsPool = CopPool()
    private val enemies by lazy { engine.getEntitiesFor(allOf(EnemyComponent::class).exclude(RemoveComponent::class).get()) }
    private val players by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()) }

    private fun copReset(entity: Entity) {
        (entity as Cop).reset()
        val transform = entity.transform
        val animation = entity.animation

        transform.offset.set(Cop.X_SPRITE_OFFSET, Cop.Y_SPRITE_OFFSET)
        transform.size.set(Cop.WIDTH_SPRITE_RATIO, Cop.HEIGHT_SPRITE_RATIO)
        transform.setInitialPosition(newPosition.x, newPosition.y, newPosition.z)
        animation.type = AnimationType.COP_WALK_FROM_LEFT

        engine.addEntity(entity)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val cop = entity.cop
        // next move
        when (cop.state) {
            CopState.WALK_TO_ENEMY -> entity.move.moveToPosition(Move.SlowLeft)
            CopState.ATTACK -> {
                entity.animation.type = AnimationType.COP_ATTACK
                entity.facing?.direction = FacingDirection.LEFT
                cop.attackTime += deltaTime
                if (cop.attackTime >= Gameplay.ENEMY_ATTACK_INTERVAL) {
                    cop.state = CopState.WALK_BACK
                    entity.animation.type = AnimationType.COP_WALK_FROM_LEFT
                    cop.attackTime = 0f
                    engine.getSystem<DamageSystem>().addCaughtEnemy()
                }
                return
            }
            CopState.WALK_BACK -> entity.move.moveToPosition(Move.SlowRight)
        }

        if (transform.position.x <= 0f || transform.position.x >= WorldDimens.F_WIDTH - 1) {
            cop.isMissed = true
            removeCopFromScreen(entity)
            return
        }

        val boundingRect = Rectangle(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
        enemies.forEach { enemyComp ->
            with(enemyComp.transform) {
                val enemyBound = Rectangle(position.x, position.y, size.x, size.y)
                if (boundingRect.overlaps(enemyBound) && enemyComp.enemy.state == EnemyState.WALK_STRAIGHT) {
                    cop.state = CopState.ATTACK
                    enemyComp.enemy.state = EnemyState.UNDER_ATTACK
                    return
                }
            }
        }

        players.forEach { playerComp ->
            with(playerComp.transform) {
                val playerBound = Rectangle(position.x, position.y, size.x, size.y)
                if (boundingRect.overlaps(playerBound) && cop.state == CopState.WALK_BACK) {
                    playerComp.player.restoreCop()
                    removeCopFromScreen(entity)
                }
            }
        }
    }

    fun addCop(position: Vector2) {
        newPosition.set(position, 1f)
        val newCop = copsPool.obtain()
        activeCops.add(newCop)
        copReset(newCop)
    }

    private fun removeCopFromScreen(entity: Entity) {
        activeCops.removeValue(entity as Cop, false)
        copsPool.free(entity)
        entity.addComponent<RemoveComponent>(engine)
        if (entity.cop.isMissed) engine.getSystem<DamageSystem>().addMissedCop()
    }
}