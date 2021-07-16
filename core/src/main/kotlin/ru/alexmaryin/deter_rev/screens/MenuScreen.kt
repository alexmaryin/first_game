package ru.alexmaryin.deter_rev.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.scrollPane
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.engine.utils.scoresList
import ru.alexmaryin.deter_rev.ui.MenuUI
import ru.alexmaryin.deter_rev.ui.ModalTextDialogUI
import ru.alexmaryin.deter_rev.ui.SettingsUI
import ru.alexmaryin.deter_rev.values.Gameplay
import ru.alexmaryin.deter_rev.values.MusicAssets

class MenuScreen(game: StartWindow) : GameScreen(game) {

    private val gameplay by lazy { GameplayScreen(game) }

    private val settingsDialog = ModalTextDialogUI("Назад", "", 0.8f, 0.8f) {
        defaults().fill().expand()
        background = skin.getDrawable("black_transparent_back")
        add(SettingsUI(game).table)
    }

    private val creditsDialog = ModalTextDialogUI("Назад", "", 0.8f, 0.8f) {
        defaults().fill().expand()
        add(scene2d.scrollPane("default_scroll") {
            setScrollbarsVisible(true)
            fadeScrollBars = true
            variableSizeKnobs = false

            label(Gameplay.CREDITS, "black") {
                wrap = true
                setFontScale(2f)
                setAlignment(Align.topLeft)
                pad(20f)
            }
        })
        pack()
    }

    private val ui by lazy {
        MenuUI().apply {
            newGameButton.onClick {
                if (!game.containsScreen<GameplayScreen>()) game.addScreen(gameplay)
                game.setScreen<GameplayScreen>()
            }
            settingsButton.onClick {
                settingsDialog.show(stage)
            }
            creditsButton.onClick {
                creditsDialog.show(stage)
            }
            quitButton.onClick {
                Gdx.app.exit()
            }
        }
    }

    override fun show() {
        stage.clear()
        ui.updateScores(game.preferences.scoresList("high_scores"))
        stage += ui.table
        if (!game.assets.isLoaded(MusicAssets.INTRO_HELP.descriptor) )
            game.audioService.play(MusicAssets.INTRO_HELP)
    }

    override fun render(delta: Float) {

        game.audioService.update(delta)

        stage.run {
            uiViewport.apply()
            act(delta)
            draw()
        }
    }
}