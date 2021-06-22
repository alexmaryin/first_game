package ru.alexmaryin.firstgame.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.get
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.engine.events.GameEvent
import ru.alexmaryin.firstgame.engine.events.GameEventsListener
import ru.alexmaryin.firstgame.engine.events.LevelUp
import ru.alexmaryin.firstgame.ui.GameplayUI
import ru.alexmaryin.firstgame.values.*
import kotlin.math.min

private val log = logger<GameplayScreen>()

enum class GameState {
    PLAY,
    PAUSED
}

class GameplayScreen(
    game: StartWindow,
    private val engine: Engine = game.engine
) : GameScreen(game), GameEventsListener {

    private var state = GameState.PAUSED
    private val atlas = game.assets[TextureAtlases.GRAPHIC_ATLAS.descriptor]
    private val backTexture = WorldDimens.BACK_LAYER_Z to atlas.findRegion(GameAssets.BACK_LAYER)
    private val frontTextures = GameAssets.FRONT_LAYERS.mapIndexed { index, region ->
        WorldDimens.IN_FRONT_LAYERS_Z[index] to atlas.findRegion(region)
    }.toMap()
    private val ui by lazy { GameplayUI(stage).apply {
        pauseButton.onClick {
            if (isChecked) pauseGame() else resumeGame()
        }
        quitButton.onClick { hide() }
    } }

    override fun show() {
        val lastScores: Int? = game.preferences["last_scores"]
        log.debug { "Main game play screen showing" }
        log.debug { lastScores?.let { "Last saved scores is $it" } ?: "No last scores saved in preferences yet."  }

        stage.clear()
        stage += ui
    }

    fun startGame() {
        state = GameState.PLAY
        game.audioService.play(MusicAssets.GAME_MUSIC_UP_10)
        EventDispatcher.subscribeOn<LevelUp>(this)

        engine.entity {
            with<TransformComponent> {
                size.set(WorldDimens.WIDTH, WorldDimens.HEIGHT)
                offset.set(Vector2.Zero)
                setInitialPosition(0f, 0f, backTexture.first)
            }
            with<GraphicComponent> { sprite.setRegion(backTexture.second) }
        }

        frontTextures.forEach { (layer, texture) ->
            engine.entity {
                with<TransformComponent> {
                    size.set(WorldDimens.WIDTH, WorldDimens.HEIGHT)
                    offset.set(Vector2.Zero)
                    setInitialPosition(0f, 0f, layer)
                }
                with<GraphicComponent> { sprite.setRegion(texture) }
            }
        }

        engine.entity {
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
//        game.setScreen<MenuScreen>()
    }

    private fun resumeGame() {
        state = GameState.PLAY
        game.resumeEngine()
    }

    override fun onEventDelivered(event: GameEvent) {
        if (event is LevelUp && event.level >= 2) {
            game.audioService.play(MusicAssets.GAME_MUSIC_FROM_10)
            EventDispatcher.removeSubscriptions(this)
        }

    }

    override fun render(delta: Float) {

        val realDelta = min(delta, Gameplay.MIN_DELTA_TME)

        engine.update(realDelta)
        game.audioService.update(delta)


        stage.run {
            viewport.apply()
            act(realDelta)
            draw()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            when (state) {
                GameState.PAUSED -> resumeGame()
                GameState.PLAY -> pauseGame()
            }
        }
    }
}