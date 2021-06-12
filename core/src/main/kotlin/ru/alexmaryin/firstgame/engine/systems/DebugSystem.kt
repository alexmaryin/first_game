package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.engine.components.PlayerComponent
import ru.alexmaryin.firstgame.engine.components.player
import ru.alexmaryin.firstgame.engine.components.transform

class DebugSystem : IntervalIteratingSystem(
    allOf(PlayerComponent::class).get(), Gameplay.DEBUG_UPDATE_RATE
) {

    private var enableDebugInfo = false
    private val log = logger<DebugSystem>()

    init { setProcessing(true) }

    override fun processEntity(entity: Entity) {

        if (Gdx.input.isKeyPressed(Input.Keys.F12)) {
            enableDebugInfo = !enableDebugInfo
            log.debug { "Debug info ${if (enableDebugInfo) "enabled" else "disabled"}" }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F11)) {
            log.debug { "Forced game over"}
            entity.player?.missedEnemies = Gameplay.MAX_MISSED_ENEMIES
        }

        if (enableDebugInfo) {
            val transform = entity.transform
            val player = entity.player
            require(player != null)
            Gdx.graphics.setTitle("pos:${transform.position} caught:${player.enemiesCaught} missed:${player.missedEnemies}")
        } else {
            Gdx.graphics.setTitle(Gameplay.DEFAULT_TITLE)
        }
    }
}