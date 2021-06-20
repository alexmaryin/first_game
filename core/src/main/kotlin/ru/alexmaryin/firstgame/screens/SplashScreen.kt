package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets
import ru.alexmaryin.firstgame.values.TextureAtlases
import ru.alexmaryin.firstgame.values.Textures


class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val log = logger<SplashScreen>()
    private lateinit var startLabel: Label
    private lateinit var progressBar: Image
    private lateinit var gameplay: GameplayScreen
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
            startLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
        }
        setupUI()
    }

    private fun setupUI() {
        stage.actors {
            table {
                defaults().fillX().expandX()
                label("DETER REVOLUTION", "red") {
                    setAlignment(Align.center)
                    setFontScale(3f)
                    wrap = true
                }
                row()
                startLabel = label("Нажмите для старта", "default") {
                    setAlignment(Align.center)
                    wrap = true
                    color.a = 0f
                    onClick { startTouched = true }
                }
                row()
                stack { cell ->
                    progressBar = image("UI_FULLBAR") { scaleX = 0f }
                    label("Загрузка...", "default") { setAlignment(Align.center) }
                    cell.padLeft(20f).padRight(20f)
                }
                row()
                checkBox(
                    " Звук в игре (${if (game.preferences["audio_on", true]) "включен" else "выключен"})",
                    "checkBox"
                ) {
                    padTop(20f)
                    isChecked = game.preferences["audio_on", true]
                    onChange {
                        setText(" Звук в игре (${if (isChecked) "включен" else "выключен"})")
                        game.preferences.flush {
                            this["audio_on"] = isChecked
                        }
                        game.setAudio(isChecked)
                    }
                }
                row()
                setFillParent(true)
                pack()
            }
        }
    }

    override fun hide() {
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

        progressBar.scaleX = assets.progress.percent
        stage.run {
            uiViewport.apply()
            act()
            draw()
        }
    }
}