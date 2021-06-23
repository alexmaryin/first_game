package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.utils.Align
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.scene2d.imageButton
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ru.alexmaryin.deter_rev.values.Gameplay

class GameplayUI(stage: Stage) : Group(), GameEventsHandler {

    private val remainingCopsLabel = scene2d.label("Полицейских: ${Gameplay.MAX_AVAILABLE_COPS}", "default_bordered") {
        setFontScale(2f)
        setPosition(20f, 30f)
    }
    private val missedEnemiesLabel = scene2d.label("Пропущено: 0", "default_bordered") {
        setFontScale(2f)
        setPosition(40f + remainingCopsLabel.width * 2f, 30f)
    }
    private val levelLabel = scene2d.label("Уровень: 1", "default_bordered") {
        setFontScale(2f)
        color = Color.YELLOW
        setPosition((stage.width - width) / 2f, stage.height - 50f)
    }
    private val eventLabel = scene2d.label("", "red") {
        setFontScale(3f)
        color.a = 0f
        setAlignment(Align.center)
        setBounds(0f, 0f, stage.width, stage.height)
    }
    val quitButton = scene2d.imageButton("red_circle_button") {
        setPosition(20f, stage.height - 70f)
        setSize(50f, 50f)
        imageCell.size(30f, 30f).align(Align.center)
        color.a = 0.7f
    }
    val pauseButton = scene2d.imageButton("orange_circle_button") {
        setPosition(stage.width - 70f, stage.height - 70f)
        setSize(50f, 50f)
        imageCell.size(30f, 30f).align(Align.center)
        color.a = 0.7f
    }

    init {
        isTransform = false
        this += quitButton
        this += pauseButton
        this += eventLabel
        this += levelLabel
        this += remainingCopsLabel
        this += missedEnemiesLabel
    }

    override fun updateEnemiesCaught(count: Int) {}

    override fun updateEnemiesMissed(count: Int) {
        with (missedEnemiesLabel) {
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