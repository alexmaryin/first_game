package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ru.alexmaryin.firstgame.StartWindow

abstract class GameScreen(
    val game: StartWindow,
    val assets: AssetStorage = game.assets,
    val stage: Stage = game.stage,
    private val viewport: Viewport = game.viewport,
    val uiViewport: Viewport = game.uiViewport,
    ) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        stage.viewport.update(width, height, true)
    }
}
