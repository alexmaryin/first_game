package ru.alexmaryin.firstgame.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import ktx.app.KtxScreen
import ru.alexmaryin.firstgame.StartWindow

abstract class GameScreen(
    val game: StartWindow,
    val batch: Batch = game.batch,
    val engine: Engine = game.engine
) : KtxScreen