package ru.alexmaryin.deter_rev.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.get
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.engine.components.*
import ru.alexmaryin.deter_rev.engine.events.*
import ru.alexmaryin.deter_rev.ui.DialogResult
import ru.alexmaryin.deter_rev.ui.GameplayUI
import ru.alexmaryin.deter_rev.ui.ModalTextDialogUI
import ru.alexmaryin.deter_rev.values.*
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

    private var firstMusic = true
    private var state = GameState.PAUSED
    private val atlas = game.assets[TextureAtlases.GRAPHIC_ATLAS.descriptor]
    private val backTexture = WorldDimens.BACK_LAYER_Z to atlas.findRegion(GameAssets.BACK_LAYER)
    private val frontTextures = GameAssets.FRONT_LAYERS.mapIndexed { index, region ->
        WorldDimens.IN_FRONT_LAYERS_Z[index] to atlas.findRegion(region)
    }.toMap()
    private val exitDialog = ModalTextDialogUI(
        "Да", "Нет",
        0.5f, 0.5f
    ) {
        defaults().fill().expand()
        add(scene2d.label("Хотите выйти?", "black") {
            wrap = true
            setFontScale(2f)
            setAlignment(Align.center)
            pad(20f)
        })
        pack()
    }
    private val ui by lazy {
        GameplayUI().apply {
            pauseButton.onClick { if (isChecked) pauseGame() else resumeGame() }
            quitButton.onClick {
                pauseGame()
                exitDialog.show(stage)
            }
        }
    }

    override fun show() {
        val lastScores: Int? = game.preferences["last_scores"]
        log.debug { "Main game play screen showing" }
        log.debug { lastScores?.let { "Last saved scores is $it" } ?: "No last scores saved in preferences yet." }

        stage.clear()
        stage += ui.table.apply { setFillParent(true) }
    }

    fun startGame() {
        state = GameState.PLAY
        game.audioService.play(MusicAssets.GAME_MUSIC_UP_10)
        EventDispatcher.subscribeOn<LevelUp>(this)
        EventDispatcher.subscribeOn<DialogExit>(this)
        EventDispatcher.subscribeOn<CopCatchEnemy>(this)

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
            with<PlayerComponent> { uiHandler = ui }
            with<FacingComponent>()
            with<MoveComponent>()
            with<TransformComponent> {
                size.set(Entities.CAR_WIDTH_SPRITE_RATIO, Entities.CAR_HEIGHT_SPRITE_RATIO)
                offset.set(Entities.CAR_X_SPRITE_OFFSET, Entities.CAR_Y_SPRITE_OFFSET)
                setInitialPosition(14f, 2f, 1f)
            }
            with<GraphicComponent>()
        }
    }

    private fun pauseGame() {
        state = GameState.PAUSED
        game.pauseEngine()
    }

    private fun resumeGame() {
        state = GameState.PLAY
        game.resumeEngine()
    }

    override fun onEventDelivered(event: GameEvent) {
        when (event) {
            is LevelUp -> {
                ui.updateLevel(event.level)
                if (event.level >= 5 && firstMusic) {
                    game.audioService.play(MusicAssets.GAME_MUSIC_FROM_10)
                    firstMusic = false
                }
            }
            is CopCatchEnemy -> {
                ui.showMessage(Gameplay.CATCH_PHRASES.random())
            }
            is DialogExit -> {
                if (event.result == DialogResult.LEFT) {
                    log.debug { "Exit game invoked!!!" }
                } else {
                    resumeGame()
                }
            }
            else -> {
            }
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
            ui.pauseButton.fire(InputEvent().apply { type = InputEvent.Type.touchDown })
            ui.pauseButton.fire(InputEvent().apply { type = InputEvent.Type.touchUp })
        }
    }
}