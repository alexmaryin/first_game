package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import ru.alexmaryin.deter_rev.engine.events.DialogExit
import ru.alexmaryin.deter_rev.engine.events.EventDispatcher
import ru.alexmaryin.deter_rev.values.WorldDimens

enum class DialogResult { LEFT, RIGHT }

class ModalTextDialogUI(
    leftButton: String,
    rightButton: String,
    private val widthRatio: Float,
    private val heightRatio: Float,
    content: Table.() -> Unit
) : Dialog("", Scene2DSkin.defaultSkin, "dialog"
) {

    init {
        buttonTable.defaults().pad(10f).maxSize(prefWidth / 3, prefHeight / 5)
        button(scene2d.textButton(leftButton, "yellow_button").apply {
            label.wrap = true
            label.setFontScale(2f)
            labelCell.padRight(10f).padLeft(10f)
            onClick { EventDispatcher.send(DialogExit(DialogResult.LEFT)); hide() }
        })
        button(scene2d.textButton(rightButton, "green_button").apply {
            label.wrap = true
            label.setFontScale(2f)
            labelCell.padRight(10f).padLeft(10f)
            onClick { EventDispatcher.send(DialogExit(DialogResult.RIGHT)); hide() }
        })
        buttonTable.pack()

        content(contentTable)

        pack()
        isModal = true
        isTransform = false
    }

    override fun hide() {
        super.hide()
    }

    override fun getPrefHeight() = WorldDimens.HEIGHT * WorldDimens.CELL_SIZE * heightRatio

    override fun getPrefWidth() = WorldDimens.WIDTH * WorldDimens.CELL_SIZE * widthRatio
}