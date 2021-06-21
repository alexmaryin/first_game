package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*
import ru.alexmaryin.firstgame.values.WorldDimens

class SettingsUI(isInitialSoundOn: Boolean, initialVolume: Float) {
    val table: KTableWidget
    private val soundLabel get() = " Звук в игре (${if (soundCheckbox.isChecked) "включен" else "выключен"})"
    private val volume get() = "Громкость ${(volumeSlider.value * 100).toInt()}%"
    val soundCheckbox: CheckBox
    val volumeSlider: Slider
    private val volumeLabel: Label

    init {
        table = scene2d.table {
            row().expandX().fillX().padTop(10f)
            volumeSlider = slider(style = "default-horizontal") { cell ->
                cell.width(WorldDimens.WIDTH * WorldDimens.CELL_SIZE * 0.7f)
                value = initialVolume
            }
            volumeLabel = label("", "secondary") { cell ->
                cell.padLeft(20f)
            }
            row().expandX().fillX().colspan(2).padTop(20f)
            soundCheckbox = checkBox("", "checkBox") {
                padTop(10f)
                isChecked = isInitialSoundOn
                label.setAlignment(Align.center)
            }
            setSoundLabel()
            setVolumeLabel()
            pack()
        }
    }

    fun setSoundLabel() = soundCheckbox.label.setText(soundLabel)

    fun setVolumeLabel() = volumeLabel.setText(volume)
}