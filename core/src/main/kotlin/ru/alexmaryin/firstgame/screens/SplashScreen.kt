package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.GameAssets
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.WorldDimens
import ru.alexmaryin.firstgame.engine.components.GraphicComponent
import ru.alexmaryin.firstgame.engine.components.TransformComponent

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
    private val player = engine.entity {
        with<TransformComponent> {
            position.set(6f, 3f, 0f)
            size.set(2f, 1f)
        }
        with<GraphicComponent> {
            sprite.run {
                setRegion(Texture(Gdx.files.internal(GameAssets.BUS)))
                setOriginCenter()
            }
        }
    }

    override fun show() {
        log.debug { "Splash screen showing" }
    }

    override fun render(delta: Float) {
        engine.update(delta)
        viewport.apply()
        batch.use(viewport.camera.combined) { batch ->
            player[GraphicComponent.mapper]?.let { graphic ->
                player[TransformComponent.mapper]?.let { transform ->
                    graphic.sprite.run {
                        rotation = transform.rotation
                        setBounds(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
                        draw(batch)
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen<MenuScreen>()
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        player[GraphicComponent.mapper]?.sprite?.texture?.dispose()
    }
}