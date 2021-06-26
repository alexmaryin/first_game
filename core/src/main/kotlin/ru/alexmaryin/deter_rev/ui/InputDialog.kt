package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.actors.setKeyboardFocus
import ktx.scene2d.*
import ru.alexmaryin.deter_rev.values.WorldDimens

class InputDialog(
    title: String,
    submit: (String?) -> Unit
) : Dialog("", Scene2DSkin.defaultSkin, "dialog") {

    private val nameInput = scene2d.textField("", "simple_input") {
        alignment = Align.center
        maxLength = 30
    }

    init {
        buttonTable.defaults().pad(10f).maxSize(prefWidth / 3, prefHeight / 5)
        button(scene2d.textButton("Отмена", "red_button").apply {
            label.setFontScale(2f)
            labelCell.padRight(10f).padLeft(10f)
            onClick { submit(null); hide() }
        })
        button(scene2d.textButton("Готово", "green_button").apply {
            label.wrap = true
            label.setFontScale(2f)
            labelCell.padRight(10f).padLeft(10f)
            onClick { submit(nameInput.text); hide() }
        })
        buttonTable.pack()

        contentTable.apply {
            row().expand().left().pad(5f)
            add(scene2d.label(title, "black") { setFontScale(2f) })
            row().expandX().fillX().pad(5f)
            add(nameInput)
            pack()
        }
        contentTable.pack()

        pack()
        isModal = true
        isTransform = false
    }

    override fun show(stage: Stage?): Dialog {
        super.show(stage)
        nameInput.setKeyboardFocus()
        return this
    }
    override fun getPrefHeight() = WorldDimens.HEIGHT * WorldDimens.CELL_SIZE * 0.5f

    override fun getPrefWidth() = WorldDimens.WIDTH * WorldDimens.CELL_SIZE * 0.8f
}