package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.*

class SplashUI {
    val table: KTableWidget
    val startLabel: Label
    private val progressBar: Image
    private val loadingLabel: Label

    init {
        table = scene2d.table {
            row().fillX().expandX()
            label("DETER REVOLUTION", "red") {
                setAlignment(Align.center)
                setFontScale(3f)
                wrap = true
            }
            row().fillX().expandX()
            startLabel = label("Нажмите для старта", "default") { cell ->
                cell.padTop(30f).padBottom(30f)
                setAlignment(Align.center)
                setFontScale(2f)
                wrap = true
                color.a = 0f
            }
            row().fillX().expandX()
            stack {
                progressBar = image("UI_FULLBAR") { scaleX = 0f }
                loadingLabel = label("", "default") { setAlignment(Align.center) }
            }
            pack()
        }
    }

    fun setProgress(value: Float) {
        progressBar.scaleX = value
        loadingLabel.setText(if (value < 1f) "Загрузка..." else "Загрузка завершена")
    }

    fun showStartLabel() { startLabel += forever(sequence(fadeIn(0.5f) + fadeOut(0.5f)))
    }
}