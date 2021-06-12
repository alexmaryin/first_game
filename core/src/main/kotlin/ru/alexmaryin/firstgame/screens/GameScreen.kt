package ru.alexmaryin.firstgame.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ru.alexmaryin.firstgame.StartWindow

abstract class GameScreen(
    val game: StartWindow,
    val batch: Batch = game.batch,
    val viewport: Viewport = game.viewport,
    val engine: Engine = game.engine
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

}
