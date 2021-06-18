package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ru.alexmaryin.firstgame.StartWindow

abstract class GameScreen(
    val game: StartWindow,
    val assets: AssetStorage = game.assets,
    private val viewport: Viewport = game.viewport
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

}
