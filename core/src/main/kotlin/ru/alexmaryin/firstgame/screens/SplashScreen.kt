package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {
    override fun show() {
        log.debug { "Splash screen showing" }
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen<MenuScreen>()
        }
    }
}