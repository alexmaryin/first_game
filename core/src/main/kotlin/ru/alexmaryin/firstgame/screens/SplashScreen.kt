package ru.alexmaryin.firstgame.screens

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.values.TextureAtlases
import ru.alexmaryin.firstgame.values.Textures

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val log = logger<SplashScreen>()

    override fun show() {
        val startTime = System.currentTimeMillis()
        val assetsRefs = gdxArrayOf(
            Textures.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlases.values().map { assets.loadAsync(it.descriptor) },
        ).flatten()

        KtxAsync.launch {
            assetsRefs.joinAll()
            log.debug { "All assets loaded in ${System.currentTimeMillis() - startTime} ms" }
            game.addScreen(MenuScreen(game))
            val gameplay = GameplayScreen(game)
            game.addScreen(gameplay)
            game.setScreen<GameplayScreen>()
            game.removeScreen<SplashScreen>()
            gameplay.startGame()
            dispose()
        }
    }
}