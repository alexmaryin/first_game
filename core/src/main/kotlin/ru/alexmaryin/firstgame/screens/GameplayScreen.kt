package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.entities.PoliceCar
import ru.alexmaryin.firstgame.engine.systems.AnimationSystem
import ru.alexmaryin.firstgame.engine.systems.CopSystem
import ru.alexmaryin.firstgame.engine.systems.EnemySystem
import ru.alexmaryin.firstgame.engine.systems.SnapMoveSystem
import ru.alexmaryin.firstgame.values.Gameplay
import kotlin.math.min

private val log = logger<GameplayScreen>()

enum class GameState {
    PLAY,
    PAUSED
}

class GameplayScreen(game: StartWindow) : GameScreen(game) {

    private val player = PoliceCar(engine)
    private var state = GameState.PAUSED

    override fun show() {
        log.debug { "Splash screen showing" }
    }

    fun startGame() {
        state = GameState.PLAY
        engine.addEntity(player)
    }

    private fun pauseGame() {
        state = GameState.PAUSED
        engine.getSystem<SnapMoveSystem>().setProcessing(false)
        engine.getSystem<AnimationSystem>().setProcessing(false)
        engine.getSystem<EnemySystem>().setProcessing(false)
        engine.getSystem<CopSystem>().setProcessing(false)
        game.setScreen<MenuScreen>()
    }

    private fun resumeGame() {
        state = GameState.PLAY
        engine.getSystem<SnapMoveSystem>().setProcessing(true)
        engine.getSystem<AnimationSystem>().setProcessing(true)
        engine.getSystem<EnemySystem>().setProcessing(true)
        engine.getSystem<CopSystem>().setProcessing(true)
    }

    override fun render(delta: Float) {

        engine.update(min(delta, Gameplay.MIN_DELTA_TME))

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            when (state) {
                GameState.PAUSED -> resumeGame()
                GameState.PLAY -> pauseGame()
            }
        }
    }
}