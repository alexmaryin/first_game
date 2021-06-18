package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.allOf
import ktx.ashley.exclude
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.CopCatchEnemy
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.engine.events.PlayerRestoresCop

class CollisionSystem : IteratingSystem(
    allOf(CopComponent::class).exclude(RemoveComponent::class).get()
) {

    private val enemies by lazy { engine.getEntitiesFor(allOf(EnemyComponent::class).exclude(RemoveComponent::class).get()) }
    private val players by lazy { engine.getEntitiesFor(allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()) }

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val copRect = with(entity.transform) {
            Rectangle(interpolatedPosition.x, interpolatedPosition.y, size.x, size.y)
        }

        when(entity.cop.state) {
            CopState.WALK_TO_ENEMY -> enemies.forEach { enemyComp ->
                with(enemyComp.transform) {
                    val enemyBound = Rectangle(interpolatedPosition.x - size.x / 2, interpolatedPosition.y, size.x, size.y)
                    if (copRect.overlaps(enemyBound) && enemyComp.enemy.state == EnemyState.WALK_STRAIGHT) {
                        EventDispatcher.send(CopCatchEnemy(entity, enemyComp))
                        return
                    }
                }
            }

            CopState.WALK_BACK -> players.forEach { playerComp ->
                with(playerComp.transform) {
                    val playerBound = Rectangle(interpolatedPosition.x, interpolatedPosition.y, size.x, size.y)
                    if (copRect.overlaps(playerBound) && entity.cop.state == CopState.WALK_BACK) {
                        playerComp.player.restoreCop()
                        EventDispatcher.send(PlayerRestoresCop(entity))
                        return
                    }
                }
            }

            else -> {}
        }
    }
}