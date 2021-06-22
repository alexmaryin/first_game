package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.scene2d.*
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.WorldDimens

class IntroUI : Dialog("", Scene2DSkin.defaultSkin, "dialog"
) {
    var isShowAgain = true

    init {
        buttonTable.defaults().pad(10f).maxHeight(100f)
        button(scene2d.textButton("Понятно", "yellow_button").apply {
            height = 30f

            label.setFontScale(2f)
            onClick { hide() }
        })
        button(scene2d.textButton("Больше не показывать", "green_button").apply {
            height = 30f
            label.wrap = true
            label.setFontScale(2f)
            labelCell.padRight(10f).padLeft(10f)
            onClick { isShowAgain = false; hide() }
        })
        buttonTable.pack()

        contentTable.defaults().fill().expand()
        contentTable.add(scene2d.scrollPane("default_scroll") {
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
        contentTable.pack()
        pack()

        isTransform = false
    }

    override fun getPrefHeight() = WorldDimens.HEIGHT * WorldDimens.CELL_SIZE * 0.8f

    override fun getPrefWidth() = WorldDimens.WIDTH * WorldDimens.CELL_SIZE * 0.8f
}