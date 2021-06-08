package ru.alexmaryin.firstgame

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.screens.MenuScreen
import ru.alexmaryin.firstgame.screens.SplashScreen

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create a game instance" }
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        setScreen<SplashScreen>()
    }
}