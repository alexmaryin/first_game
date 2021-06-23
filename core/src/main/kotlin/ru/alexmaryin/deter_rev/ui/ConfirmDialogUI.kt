package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import ru.alexmaryin.deter_rev.engine.events.DialogExit
import ru.alexmaryin.deter_rev.engine.events.EventDispatcher
import ru.alexmaryin.deter_rev.values.WorldDimens

enum class DialogResult { OK, CANCEL }

class ConfirmDialogUI(question: String, okButton: String = "OK", cancelButton: String = "Отмена") : Dialog("", Scene2DSkin.defaultSkin, "dialog") {

    init {
        buttonTable.defaults().pad(10f).maxSize(prefWidth / 3, prefHeight / 5)
        button(scene2d.textButton(cancelButton, "red_button").apply {
            label.setFontScale(2f)
            onClick { EventDispatcher.send(DialogExit(DialogResult.CANCEL)); hide() }
        })
        button(scene2d.textButton(okButton, "green_button").apply {
            label.setFontScale(2f)
            onClick { EventDispatcher.send(DialogExit(DialogResult.OK)); hide() }
        })
        buttonTable.pack()

        contentTable.defaults().fill().expand()
        contentTable.add(scene2d.label(question, "black") {
            wrap = true
            setFontScale(2f)
            setAlignment(Align.center)
            pad(20f)
        })
        contentTable.pack()
        pack()

        isTransform = false
    }

    override fun getPrefHeight() = WorldDimens.HEIGHT * WorldDimens.CELL_SIZE * 0.5f

    override fun getPrefWidth() = WorldDimens.WIDTH * WorldDimens.CELL_SIZE * 0.5f
}