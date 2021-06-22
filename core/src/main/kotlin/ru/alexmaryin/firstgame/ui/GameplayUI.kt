package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import ktx.actors.centerPosition
import ktx.actors.plusAssign
import ktx.scene2d.*
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.screens.GameState
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
    val quitButton = scene2d.imageButton("red_circle_button") {
        setPosition(20f, stage.height - 70f)
        setSize(50f, 50f)
        imageCell.size(30f, 30f)
        color.a = 0.7f
    }
    val pauseButton = scene2d.imageButton("orange_circle_button") {
        setPosition(stage.width - 70f, stage.height - 70f)
        setSize(50f, 50f)
        imageCell.size(30f, 30f)
        color.a = 0.7f
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