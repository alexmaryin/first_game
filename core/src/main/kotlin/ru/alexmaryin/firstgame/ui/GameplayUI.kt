package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.actors.centerPosition
import ktx.actors.plusAssign
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.values.Gameplay

class GameplayUI(stage: Stage) : Group(), GameEventsListener {
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
        centerPosition(stage.width, stage.height)
        color.a = 0f
    }
    val quitButton = scene2d.textButton("✕", "red_circle_button") {
        setPosition(20f, stage.height - 50f)
        setSize(50f, 50f)
        color.a = 0.5f
        label.color.a = 0.7f
        label.setFontScale(2f)
    }
    val pauseButton = scene2d.textButton("⏸", "yellow_circle_button") {
        setPosition(stage.width - 50f, stage.height - 50f)
        setSize(50f, 50f)
        color.a = 0.5f
        label.color.a = 0.7f
        label.setFontScale(2f)
    }

    init {
        EventDispatcher.subscribeOn<EnemyMissed>(this)
        EventDispatcher.subscribeOn<EnemyCaught>(this)
        EventDispatcher.subscribeOn<CopMissed>(this)
        EventDispatcher.subscribeOn<CopCatchEnemy>(this)
        EventDispatcher.subscribeOn<PlayerRestoresCop>(this)
        EventDispatcher.subscribeOn<LevelUp>(this)

        this += quitButton
        this += pauseButton
        this += eventLabel
        this += levelLabel
        this += remainingCopsLabel
        this += missedEnemiesLabel
    }

    override fun onEventDelivered(event: GameEvent) {
        when(event) {
            is CopCatchEnemy -> {}
            is CopMissed -> {}
            is EnemyCaught -> {}
            is EnemyMissed -> {}
            is GameOver -> {}
            is LevelUp -> {}
            is PlayerRestoresCop -> {}
        }
    }
}