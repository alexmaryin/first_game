package ru.alexmaryin.deter_rev.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import ktx.actors.stage
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.deter_rev.engine.components.*
import ru.alexmaryin.deter_rev.engine.events.EventDispatcher
import ru.alexmaryin.deter_rev.engine.events.GameOver
import ru.alexmaryin.deter_rev.values.Gameplay
import ru.alexmaryin.deter_rev.values.WorldDimens

class DebugSystem(private val batch: SpriteBatch, private val debugTexture: Texture) : IntervalIteratingSystem(
    allOf(PlayerComponent::class).get(), Gameplay.DEBUG_UPDATE_RATE
) {

    private var enableDebugInfo = false
    private var isGridEnabled = false
    private lateinit var debugGrid: Entity
    private val log = logger<DebugSystem>()

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
            EventDispatcher.send(GameOver(5))
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            isGridEnabled = !isGridEnabled
            if (isGridEnabled) {
                log.debug { "Showing debug grid" }
                debugGrid = engine.entity {
                    with<GraphicComponent> {
                        sprite.setAlpha(0.2f)
                        sprite.setRegion(debugTexture)
                    }
                    with<TransformComponent> {
                        size.set(WorldDimens.WIDTH, WorldDimens.HEIGHT)
                        offset.set(Vector2.Zero)
                        setInitialPosition(0f, 0f, 0f)
                    }
                }
            } else {
                log.debug { "Hide debug grid" }
                debugGrid.addComponent<RemoveComponent>(engine) { delay = 0f }
            }
        }

        if (enableDebugInfo) {
            val player = entity.player
            val damage = engine.getSystem<EventSystem>()
            Gdx.graphics.setTitle(buildString {
                append("level: ${damage.level} ")
                append("caught:${player.enemiesCaught} ")
                append("missed:${player.missedEnemies} ")
                append("cops:${player.availableCops} ")
                append("render calls:${batch.renderCalls} ")
                append("heap java/native:${Gdx.app.javaHeap / 1024 / 1024} MiB/${Gdx.app.nativeHeap / 1024 / 1024} MiB ")
                append("FPS: ${Gdx.graphics.framesPerSecond} ")
            })
        } else {
            Gdx.graphics.setTitle(Gameplay.DEFAULT_TITLE)
        }
    }
}