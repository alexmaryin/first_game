package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.engine.components.*

class DamageSystem : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val player = entity.player
        require(player != null)

        if (player.missedEnemies >= Gameplay.MAX_MISSED_ENEMIES) {
            engine.getSystem<MoveSystem>().setProcessing(false)
            entity.addComponent<RemoveComponent>(engine) {
                delay = Gameplay.GAME_OVER_DELAY
            }
        }
    }
}