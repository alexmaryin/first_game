package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.WorldDimens

class DebugSystem(private val batch: SpriteBatch) : IntervalIteratingSystem(
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
            entity.player.missedEnemies = Gameplay.MAX_MISSED_ENEMIES
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
                        setInitialPosition(0f, 0f, 5f)
                        size.set(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
                    }
                }
            } else {
                log.debug { "Hide debug grid" }
                debugGrid.addComponent<RemoveComponent>(engine) { delay = 0f }
                debugGrid.graphic.sprite.texture.dispose()
            }
        }

        if (enableDebugInfo) {
            val transform = entity.transform
            val player = entity.player
            val enemies = engine.getSystem<EnemySystem>()
            val damage = engine.getSystem<DamageSystem>()
            Gdx.graphics.setTitle(buildString {
//                append("pos:${transform.position} ")
                append("level: ${damage.gameLevel} ")
                append("speed: ${0.15f + damage.gameLevel / 10f} ")
                append("caught:${player.enemiesCaught} ")
                append("missed:${player.missedEnemies} ")
//                append("enemies:${enemies.enemiesOnScreen} ")
                append("cops:${player.availableCops} ")
//                append("render calls:${batch.renderCalls} ")
                append("heap java/native:${Gdx.app.javaHeap / 1024 / 1024} MiB/${Gdx.app.nativeHeap / 1024 / 1024} MiB ")
            })
        } else {
            Gdx.graphics.setTitle(Gameplay.DEFAULT_TITLE)
        }
    }
}