package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.engine.components.PlayerComponent
import ru.alexmaryin.firstgame.engine.components.RemoveComponent
import ru.alexmaryin.firstgame.engine.components.TransformComponent
import ru.alexmaryin.firstgame.engine.components.player
import ru.alexmaryin.firstgame.values.Gameplay
import kotlin.math.max

class DamageSystem : IteratingSystem(
    allOf(PlayerComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()
) {

    private var getMissedEnemy = 0
    private var getMissedCop = 0
    private var getCaughtEnemy = 0

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val player = entity.player

        if (getMissedEnemy > 0) {
            player.missedEnemies += getMissedEnemy
            getMissedEnemy = 0
        }

        if (getMissedCop > 0) {
            player.availableCops = max(player.availableCops - getMissedCop, 0)
            getMissedCop = 0
        }

        if (getCaughtEnemy > 0) {
            player.enemiesCaught += getCaughtEnemy
            getCaughtEnemy = 0
        }

        if (player.missedEnemies >= Gameplay.MAX_MISSED_ENEMIES)
            gameOver(entity)
    }

    fun addMissedEnemy() {
        getMissedEnemy += 1
    }

    fun addMissedCop() {
        getMissedCop += 1
    }

    fun addCaughtEnemy() {
        getCaughtEnemy += 1
    }

    private fun gameOver(entity: Entity) {
        engine.getSystem<SnapMoveSystem>().setProcessing(false)
        engine.getSystem<AnimationSystem>().setProcessing(false)
        engine.getSystem<EnemySystem>().setProcessing(false)
        engine.getSystem<CopSystem>().setProcessing(false)
        Gdx.input.vibrate(1000)
        entity.addComponent<RemoveComponent>(engine) {
            delay = Gameplay.GAME_OVER_DELAY
        }
    }
}