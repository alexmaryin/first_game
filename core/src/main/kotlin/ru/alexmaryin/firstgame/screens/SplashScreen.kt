package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.WorldDimens

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
    private val bus = Texture(Gdx.files.internal("graphic/spr_camper_0.png"))
    private val busSprite = Sprite(bus).apply {
        setSize(texture.width / WorldDimens.F_CELL_SIZE, texture.height / WorldDimens.F_CELL_SIZE)
    }

    override fun show() {
        log.debug { "Splash screen showing" }
        busSprite.setPosition(0f, 0f)
    }

    override fun render(delta: Float) {
        batch.use(viewport.camera.combined) {
            busSprite.draw(it)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen<MenuScreen>()
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        bus.dispose()
    }
}