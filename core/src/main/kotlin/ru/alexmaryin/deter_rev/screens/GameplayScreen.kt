package ru.alexmaryin.deter_rev.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.ashley.entity
import ktx.ashley.with
import ktx.async.KtxAsync
import ktx.preferences.flush
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.engine.components.*
import ru.alexmaryin.deter_rev.engine.events.*
import ru.alexmaryin.deter_rev.engine.utils.scoresList
import ru.alexmaryin.deter_rev.engine.utils.toScoresLine
import ru.alexmaryin.deter_rev.ui.*
import ru.alexmaryin.deter_rev.values.*
import kotlin.math.min

//private val log = logger<GameplayScreen>()

enum class GameState { PLAY, PAUSED }

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

    private val pauseDialog = ModalTextDialogUI(
        "Выйти", "Продолжить",
        0.8f, 0.8f
    ) {
        defaults().fill().expand()
        background = skin.getDrawable("black_transparent_back")
        add(SettingsUI(game).table)
    }

    private val ui by lazy {
        GameplayUI().apply {
            pauseButton.onClick { if (isChecked) pauseGame(pauseDialog) else resumeGame() }
            quitButton.onClick { pauseButton.toggle(); pauseGame(exitDialog) }
        }
    }

    override fun show() {
        stage.clear()
        ui.reset()
        stage += ui.table.apply { setFillParent(true) }
        startGame()
    }

    private fun startGame() {
        state = GameState.PLAY
        game.audioService.play(MusicAssets.GAME_MUSIC_UP_10)
        EventDispatcher.subscribeOn<LevelUp>(this)
        EventDispatcher.subscribeOn<DialogExit>(this)
        EventDispatcher.subscribeOn<CopCatchEnemy>(this)
        EventDispatcher.subscribeOn<GameOver>(this)

        engine.entity {
            with<TransformComponent> {
                size.set(WorldDimens.WIDTH, WorldDimens.HEIGHT)
                setInitialPosition(0f, 0f, backTexture.first)
            }
            with<GraphicComponent> { sprite.setRegion(backTexture.second) }
        }

        frontTextures.forEach { (layer, texture) ->
            engine.entity {
                with<TransformComponent> {
                    size.set(WorldDimens.WIDTH, WorldDimens.HEIGHT)
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

        resumeGame()
    }

    private fun pauseGame(dialog: ModalTextDialogUI) {
        state = GameState.PAUSED
        game.pauseEngine()
        dialog.show(stage)
    }

    private fun resumeGame() {
        state = GameState.PLAY
        game.resumeEngine()
    }

    private fun stopGame(score: Int) {
        if (score > 0) {
            val highScores = game.preferences.scoresList("high_scores")
            val insertPlace = (highScores.indexOfLast { it.first > score } + 1).coerceAtLeast(0)
            val inputDialog = InputDialog("Введите имя для таблицы рекордов:") { name ->
                if (name != null && name.isNotBlank()) {
                    highScores.add(insertPlace, Pair(score, name))
                    game.preferences.flush { toScoresLine("high_scores", highScores) }
                }
                goToMenu()
            }
            if (insertPlace < 10) {
                inputDialog.show(stage)
            } else {
                goToMenu()
            }
        } else goToMenu()
    }

    private fun goToMenu() {
        game.resetEngine()
        game.setScreen<MenuScreen>()
    }

    override fun onEventDelivered(event: GameEvent) {
        when (event) {
            is GameOver -> {
                game.pauseEngine()
                KtxAsync.launch {
                    ui.showMessage("Вы проиграли.\nРеволюция совершилась.", 4f)
                    delay(5000)
                    ui.showMessage("А сколько человек нужно,\nчтобы изменить историю\nвашей страны?", 6f)
                    delay(7000)
                    stopGame(event.score)
                }
            }
            is LevelUp -> {
                ui.updateLevel(event.level)
                if (event.level % 5 == 0) toggleMusic()
            }
            is CopCatchEnemy -> {
                ui.showMessage(Gameplay.CATCH_PHRASES.random())
            }
            is DialogExit -> {
                if (event.result == DialogResult.LEFT) stopGame(-1) else resumeGame()
                ui.pauseButton.toggle()
            }
            else -> {}
        }
    }

    private fun toggleMusic() {
       game.audioService.play(if (game.assets.isLoaded(MusicAssets.GAME_MUSIC_UP_10.descriptor))
           MusicAssets.GAME_MUSIC_FROM_10 else MusicAssets.GAME_MUSIC_UP_10)
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && state == GameState.PLAY) {
            ui.pauseButton.toggle()
            pauseGame(pauseDialog)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) {
            EventDispatcher.send(GameOver(35))
        }
    }
}