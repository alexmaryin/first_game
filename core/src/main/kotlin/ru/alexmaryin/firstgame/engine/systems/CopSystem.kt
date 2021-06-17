package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.entities.Cop
import ru.alexmaryin.firstgame.engine.events.CopCatchEnemy
import ru.alexmaryin.firstgame.engine.events.CopMissed
import ru.alexmaryin.firstgame.engine.events.EnemyCaught
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.MoveLeft
import ru.alexmaryin.firstgame.values.MoveRight
import ru.alexmaryin.firstgame.values.WorldDimens

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
        Cop(engine).apply {
            transform.setInitialPosition(14f, road, 0f)
            animation.type = AnimationType.COP_WALK_FROM_LEFT
            engine.addEntity(this)
        }
    }

    private fun removeCopFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
        Gdx.input.vibrate(100)
    }
}