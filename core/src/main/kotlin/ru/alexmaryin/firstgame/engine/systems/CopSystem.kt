package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.CopCatchEnemy
import ru.alexmaryin.firstgame.engine.events.CopMissed
import ru.alexmaryin.firstgame.engine.events.EnemyCaught
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.values.*

class CopSystem : IteratingSystem(
    allOf(CopComponent::class).exclude(RemoveComponent::class).get()
) {

    private val enemies by lazy { engine.getEntitiesFor(allOf(EnemyComponent::class).exclude(RemoveComponent::class).get()) }
    private val players by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()) }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val cop = entity.cop
        val level = engine.getSystem<EventSystem>().level
        val boundingRect = Rectangle(
            transform.interpolatedPosition.x, transform.interpolatedPosition.y, transform.size.x, transform.size.y
        )

        // At first, let's make next move and check overlaps with enemy or player
        when (cop.state) {
            CopState.WALK_TO_ENEMY -> {
                entity.move.moveToPosition(MoveLeft(level))
                // Check whether cop overlaps any of enemies with state walk_straight
                enemies.forEach { enemyComp ->
                    with(enemyComp.transform) {
                        val enemyBound =
                            Rectangle(interpolatedPosition.x - size.x / 2, interpolatedPosition.y, size.x, size.y)
                        if (boundingRect.overlaps(enemyBound) && enemyComp.enemy.state == EnemyState.WALK_STRAIGHT) {
                            EventDispatcher.send(CopCatchEnemy(entity, enemyComp))
                            return
                        }
                    }
                }
            }
            CopState.WALK_BACK -> {
                entity.move.moveToPosition(MoveRight(level))
                // Check whether cop overlaps player
                players.forEach { playerComp ->
                    with(playerComp.transform) {
                        val playerBound = Rectangle(interpolatedPosition.x, interpolatedPosition.y, size.x, size.y)
                        if (boundingRect.overlaps(playerBound) && cop.state == CopState.WALK_BACK) {
                            playerComp.player.restoreCop()
                            removeCopFromScreen(entity)
                            return
                        }
                    }
                }
            }
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
        // At the end, check whether cop has reached the screen border and is going to disappear
        if (transform.position.x <= 0f || transform.position.x >= WorldDimens.F_WIDTH - 1f) {
            EventDispatcher.send(CopMissed)
            removeCopFromScreen(entity)
        }
    }

    fun addCop(road: Float) {
        engine.entity {
            with<CopComponent>()
            with<MoveComponent> { initialSpeed = 0.05f }
            with<FacingComponent>()
            with<TransformComponent> {
                size.set(Entities.COP_WIDTH_SPRITE_RATIO, Entities.COP_HEIGHT_SPRITE_RATIO)
                offset.set(Entities.COP_X_SPRITE_OFFSET, Entities.COP_Y_SPRITE_OFFSET)
                setInitialPosition(14f, road, 0f)
            }
            with<AnimationComponent> { type = AnimationType.COP_WALK_FROM_LEFT }
            with<GraphicComponent>()
        }
    }

    private fun removeCopFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
        Gdx.input.vibrate(100)
    }
}