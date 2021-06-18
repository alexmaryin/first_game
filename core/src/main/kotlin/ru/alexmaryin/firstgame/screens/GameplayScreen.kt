package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.ashley.entity
import ktx.ashley.getSystem
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.systems.AnimationSystem
import ru.alexmaryin.firstgame.engine.systems.CopSystem
import ru.alexmaryin.firstgame.engine.systems.EnemySystem
import ru.alexmaryin.firstgame.engine.systems.SnapMoveSystem
import ru.alexmaryin.firstgame.values.Entities
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.WorldDimens
import kotlin.math.min

private val log = logger<GameplayScreen>()

enum class GameState {
    PLAY,
    PAUSED
}

class GameplayScreen(game: StartWindow) : GameScreen(game) {

    private var state = GameState.PAUSED
    private val backTexture = Texture(Gdx.files.internal(GameAssets.LEVEL_1_BACK))
    private val frontTexture = Texture(Gdx.files.internal(GameAssets.LEVEL_1_FRONT))

    override fun show() {
        log.debug { "Main game play screen showing" }
    }

    fun startGame() {
        state = GameState.PLAY

        val back = engine.entity {
            with<TransformComponent> {
                size.set(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
                offset.set(Vector2.Zero)
                setInitialPosition(0f, 0f, 5f)
            }
            with<GraphicComponent> { sprite.setRegion(backTexture) }
        }

        val front = engine.entity {
            with<TransformComponent> {
                size.set(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
                offset.set(Vector2.Zero)
                setInitialPosition(0f, 0f, -1f)
            }
            with<GraphicComponent> { sprite.setRegion(frontTexture) }
        }

        val player = engine.entity {
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
            with<TransformComponent> {
                size.set(Entities.CAR_WIDTH_SPRITE_RATIO, Entities.CAR_HEIGHT_SPRITE_RATIO)
                offset.set(Entities.CAR_X_SPRITE_OFFSET, Entities.CAR_Y_SPRITE_OFFSET)
                setInitialPosition(14f,2f, 1f)
            }
            with<GraphicComponent>()
        }
    }

    private fun pauseGame() {
        state = GameState.PAUSED
        game.pauseEngine()
        game.setScreen<MenuScreen>()
    }

    private fun resumeGame() {
        state = GameState.PLAY
        game.resumeEngine()
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

    override fun dispose() {
        backTexture.dispose()
        frontTexture.dispose()
        super.dispose()
    }
}