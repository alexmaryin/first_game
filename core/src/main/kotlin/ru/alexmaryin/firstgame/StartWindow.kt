package ru.alexmaryin.firstgame

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.screens.GameScreen
import ru.alexmaryin.firstgame.screens.MenuScreen
import ru.alexmaryin.firstgame.screens.SplashScreen

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<GameScreen>() {

    val batch by lazy { SpriteBatch() }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create a game instance" }
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        setScreen<SplashScreen>()
    }

    override fun dispose() {
        batch.dispose()
    }
}