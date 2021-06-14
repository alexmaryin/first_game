package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.engine.components.*

class DamageSystem : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()
) {

    private var getMissedEnemy = 0

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val player = entity.player
        require(player != null)

        if (getMissedEnemy > 0) {
            player.missedEnemies += getMissedEnemy
            getMissedEnemy = 0
        }

        if (player.missedEnemies >= Gameplay.MAX_MISSED_ENEMIES) {
            engine.getSystem<SnapMoveSystem>().setProcessing(false)
            engine.getSystem<AnimationSystem>().setProcessing(false)
            engine.getSystem<EnemySystem>().setProcessing(false)
            Gdx.input.vibrate(1000)
            entity.addComponent<RemoveComponent>(engine) {
                delay = Gameplay.GAME_OVER_DELAY
            }
        }
    }

    fun addMissedEnemy() {
        getMissedEnemy += 1
    }
}