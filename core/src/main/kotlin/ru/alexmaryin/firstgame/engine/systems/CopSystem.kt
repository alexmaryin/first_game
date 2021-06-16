package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.*
import ktx.collections.GdxArray
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.entities.Cop
import ru.alexmaryin.firstgame.engine.events.CopCatchEnemy
import ru.alexmaryin.firstgame.engine.events.CopMissed
import ru.alexmaryin.firstgame.engine.events.EnemyCaught
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.values.*

class CopSystem : IteratingSystem(
    allOf(CopComponent::class, TransformComponent::class, MoveComponent::class)
        .exclude(RemoveComponent::class).get()
), EntityListener {

    inner class CopPool : Pool<Cop>() {
        override fun newObject() = Cop(engine)
    }

//    private val log = logger<CopSystem>()

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
        val level = engine.getSystem<EventSystem>().level

        // At first, let's make next move
        when (cop.state) {
            CopState.WALK_TO_ENEMY -> entity.move.moveToPosition(MoveLeft(level))
            CopState.WALK_BACK -> entity.move.moveToPosition(MoveRight(level))
            CopState.ATTACK -> {
                cop.attackTime -= deltaTime
                entity.facing?.direction = FacingDirection.LEFT
                if (cop.attackTime <= 0) {
                    cop.state = CopState.WALK_BACK
                    entity.animation.setPreviousAnimation()
                    EventDispatcher.send(EnemyCaught)
                }
                return          // No need check other states if cop is attacking
            }
        }

        // Check whether cop overlaps any of enemies with state walk_straight
        val boundingRect = Rectangle(
            transform.interpolatedPosition.x, transform.interpolatedPosition.y, transform.size.x, transform.size.y)
        enemies.forEach { enemyComp ->
            with(enemyComp.transform) {
                val enemyBound = Rectangle(interpolatedPosition.x - size.x / 2, interpolatedPosition.y, size.x, size.y)
                if (boundingRect.overlaps(enemyBound) && enemyComp.enemy.state == EnemyState.WALK_STRAIGHT) {
                    EventDispatcher.send(CopCatchEnemy(entity, enemyComp))
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
            EventDispatcher.send(CopMissed)
            removeCopFromScreen(entity)
        }
    }

    fun addCop(position: Vector2) {
        newPosition.set(position, 0f)
        copsPool.obtain().apply {
            remove<RemoveComponent>()
            engine.addEntity(this)
            activeCops.add(this)
        }
    }

    private fun removeCopFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
    }
}