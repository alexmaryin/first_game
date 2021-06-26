package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.*
import ru.alexmaryin.deter_rev.engine.events.GameEventsHandler
import ru.alexmaryin.deter_rev.values.Gameplay

class GameplayUI : GameEventsHandler {

    val table: KTableWidget
    private val remainingCopsLabel: Label
    private val missedLabel: Label
    private val caughtLabel: Label
    private val levelLabel: Label
    private val eventLabel: Label
    val quitButton: ImageButton
    val pauseButton: ImageButton

    init {
        table = scene2d.table {
            row().pad(5f).expandX().fillX()
            quitButton = imageButton("red_circle_button") { cell ->
                cell.align(Align.left)
                cell.size (50f, 50f)
                imageCell.size(30f, 30f).align(Align.center)
                color.a = 0.7f
            }
            levelLabel = label("Уровень: 1", "default_bordered") {
                setAlignment(Align.center)
                setFontScale(2f)
                color = Color.YELLOW
            }
            pauseButton = imageButton("orange_circle_button") { cell ->
                cell.align(Align.right)
                cell.size(50f, 50f)
                imageCell.size(30f, 30f).align(Align.center)
                color.a = 0.7f
            }

            row().expand().colspan(3).center()
            eventLabel = label("", "red") {
                setFontScale(3f)
                color.a = 0f
            }

            row().expandX().pad(5f).left()
            caughtLabel = label("Счет: 0", "default_bordered") {
                setFontScale(2f)
            }
            missedLabel = label("Пропущено: 0", "default_bordered") {
                setFontScale(2f)
            }
            remainingCopsLabel = label("Полицейских: ${Gameplay.MAX_AVAILABLE_COPS}", "default_bordered") {
                setFontScale(2f)
            }
            pack()
        }
    }

    override fun updateEnemiesCaught(count: Int) {
        with(caughtLabel) {
            setText("Счет: $count")
            this += sequence(color(Color.CORAL, 0.2f) + color(Color.WHITE, 0.2f))
        }
    }

    override fun updateEnemiesMissed(count: Int) {
        with (missedLabel) {
            setText("Пропущено: $count")
            this += sequence(color(Color.RED, 0.2f) + color(Color.WHITE, 0.2f))
            invalidateHierarchy()
        }
    }

    override fun updateMissedCops(count: Int) {}

    override fun updateAvailableCops(count: Int) {
        with (remainingCopsLabel) {
            setText("Полицейских: $count")
            this += sequence(color(Color.OLIVE, 0.2f) + color(Color.WHITE, 0.2f))
            invalidateHierarchy()
        }
    }

    override fun updateLevel(level: Int) {
        with (levelLabel) {
            setText("Уровень: $level")
            this += sequence(color(Color.RED, 0.3f) + color(Color.YELLOW, 0.3f))
        }
    }

    override fun showMessage(message: String) {
        with (eventLabel) {
            setText(message)
            invalidate()
            this += sequence(fadeIn(0.5f) + delay(1f, fadeOut(0.5f)))
        }
    }
}