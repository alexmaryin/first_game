package ru.alexmaryin.deter_rev.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.actors
import ktx.scene2d.verticalGroup
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.ui.IntroUI
import ru.alexmaryin.deter_rev.ui.SettingsUI
import ru.alexmaryin.deter_rev.ui.SplashUI
import ru.alexmaryin.deter_rev.values.MusicAssets
import ru.alexmaryin.deter_rev.values.SoundAssets
import ru.alexmaryin.deter_rev.values.TextureAtlases
import ru.alexmaryin.deter_rev.values.Textures

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val log = logger<SplashScreen>()
    private lateinit var gameplay: GameplayScreen
    private val ui = SplashUI().apply {
        startLabel.onClick { startTouched = true }
    }
    private val settingsUi = SettingsUI(
        game.preferences["audio_on", true],
        game.preferences["music_volume", 0.5f]
    ).apply {
        soundCheckbox.onChange {
            game.preferences["audio_on"] = isChecked
            game.setAudio(isChecked)
            setSoundLabel()
        }
        volumeSlider.onChange {
            game.preferences["music_volume"] = value
            game.audioService.changeVolume(value)
            setVolumeLabel()
        }
    }
    private val introDialog = IntroUI()
    private var startTouched = false

    override fun show() {
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
            gameplay = GameplayScreen(game)
            log.debug { "All assets loaded in ${System.currentTimeMillis() - startTime} ms" }
            game.addScreen(gameplay)
            ui.showStartLabel()
        }

        stage.actors {
            verticalGroup {
                setFillParent(true)
                center()
                this += ui.table
                this += settingsUi.table
                invalidateHierarchy()
                pack()
            }
        }
        stage.isDebugAll = false

        if (game.preferences["show_intro", true]) introDialog.show(stage)
    }

    override fun hide() {
        game.preferences["show_intro"] = introDialog.isShowAgain
        game.preferences.flush()
        stage.clear()
        dispose()
    }

    override fun render(delta: Float) {

        game.audioService.update(delta)

        if (assets.progress.isFinished && (startTouched || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY))
            && game.containsScreen<GameplayScreen>()
        ) {
            game.setScreen<GameplayScreen>()
            game.removeScreen<SplashScreen>()
            gameplay.startGame()
        }

        ui.setProgress(assets.progress.percent)
        stage.run {
            uiViewport.apply()
            act(delta)
            draw()
        }
    }
}