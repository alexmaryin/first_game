package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.WorldDimens

class DebugSystem : IntervalIteratingSystem(
    allOf(PlayerComponent::class).get(), Gameplay.DEBUG_UPDATE_RATE
) {

    private var enableDebugInfo = false
    private var isGridEnabled = false
    private val log = logger<DebugSystem>()
    private lateinit var debugGrid: Entity

    init {
        setProcessing(true)
    }

    override fun processEntity(entity: Entity) {

        if (Gdx.input.isKeyPressed(Input.Keys.F12)) {
            enableDebugInfo = !enableDebugInfo
            log.debug { "Debug info ${if (enableDebugInfo) "enabled" else "disabled"}" }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F11)) {
            log.debug { "Forced game over" }
            entity.player?.missedEnemies = Gameplay.MAX_MISSED_ENEMIES
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            isGridEnabled = !isGridEnabled
            if (isGridEnabled) {
                log.debug { "Showing debug grid" }
                debugGrid = engine.entity {
                    with<GraphicComponent> {
                        val texture = Texture(Gdx.files.internal(GameAssets.DEBUG_GRID))
                        sprite.setAlpha(0.5f)
                        sprite.setRegion(texture)
                    }
                    with<TransformComponent> {
                        setInitialPosition(0f, 0f, 1f)
                        size.set(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
                    }
                }
            } else {
                log.debug { "Hide debug grid" }
                debugGrid.addComponent<RemoveComponent>(engine) { delay = 0f }
                debugGrid.getComponent(GraphicComponent::class.java).sprite.texture.dispose()
            }
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