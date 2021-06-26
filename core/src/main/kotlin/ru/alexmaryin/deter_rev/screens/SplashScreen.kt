package ru.alexmaryin.deter_rev.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.engine.events.DialogExit
import ru.alexmaryin.deter_rev.engine.events.EventDispatcher
import ru.alexmaryin.deter_rev.engine.events.GameEvent
import ru.alexmaryin.deter_rev.engine.events.GameEventsListener
import ru.alexmaryin.deter_rev.ui.DialogResult
import ru.alexmaryin.deter_rev.ui.ModalTextDialogUI
import ru.alexmaryin.deter_rev.ui.SettingsUI
import ru.alexmaryin.deter_rev.ui.SplashUI
import ru.alexmaryin.deter_rev.values.*

class SplashScreen(game: StartWindow) : GameScreen(game), GameEventsListener {

    private val log = logger<SplashScreen>()
    private val ui = SplashUI().apply {
        startLabel.onClick { startTouched = true }
    }
    private val introDialog = ModalTextDialogUI(
        "Понятно", "Больше не показывать",
        0.8f, 0.8f) {
        defaults().fill().expand()
        add(scene2d.scrollPane("default_scroll") {
            setScrollbarsVisible(true)
            fadeScrollBars = true
            variableSizeKnobs = false

            label(if (Gdx.app.type == Application.ApplicationType.Android) Gameplay.INTRO_HELP_MOBILE else Gameplay.INTRO_HELP, "black") {
                wrap = true
                setFontScale(2f)
                setAlignment(Align.topLeft)
                pad(20f)
            }
        })
        pack()
    }

    private var startTouched = false

    override fun show() {
        EventDispatcher.subscribeOn<DialogExit>(this)
        val startTime = System.currentTimeMillis()
        val assetsRefs = gdxArrayOf(
            Textures.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlases.values().map { assets.loadAsync(it.descriptor) },
            SoundAssets.values().map { assets.loadAsync(it.descriptor) },
        ).flatten().plus(assets.loadAsync(MusicAssets.INTRO_HELP.descriptor))

        KtxAsync.launch {
            assetsRefs.joinAll()
            game.audioService.play(MusicAssets.INTRO_HELP)
            game.addScreen(MenuScreen(game))
            log.debug { "All assets loaded in ${System.currentTimeMillis() - startTime} ms" }
            ui.showStartLabel()
        }

        stage.actors {
            verticalGroup {
                setFillParent(true)
                center()
                this += ui.table
                this += SettingsUI(game).table
                invalidateHierarchy()
                pack()
            }
        }
        stage.isDebugAll = false

        if (game.preferences["show_intro", true]) introDialog.show(stage)
    }

    override fun hide() {
        game.preferences.flush()
        stage.clear()
        dispose()
    }

    override fun render(delta: Float) {

        game.audioService.update(delta)

        if (assets.progress.isFinished && (startTouched || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
            && game.containsScreen<MenuScreen>()
        ) {
            game.setScreen<MenuScreen>()
            game.removeScreen<SplashScreen>()
        }

        ui.setProgress(assets.progress.percent)
        stage.run {
            uiViewport.apply()
            act(delta)
            draw()
        }
    }

    override fun onEventDelivered(event: GameEvent) {
        if (event is DialogExit && event.result == DialogResult.RIGHT) {
            game.preferences["show_intro"] = false
        }
    }
}