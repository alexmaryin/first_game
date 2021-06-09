package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.graphics.g2d.Batch
import ktx.app.KtxScreen
import ru.alexmaryin.firstgame.StartWindow

abstract class GameScreen(
    val game: StartWindow,
    val batch: Batch = game.batch
) : KtxScreen