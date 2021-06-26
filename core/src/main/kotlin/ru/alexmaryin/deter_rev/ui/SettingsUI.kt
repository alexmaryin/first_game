package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.preferences.get
import ktx.preferences.set
import ktx.scene2d.*
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.values.WorldDimens

class SettingsUI(private val game: StartWindow) {
    val table: KTableWidget
    private val soundLabel get() = " Звук в игре (${if (soundCheckbox.isChecked) "включен" else "выключен"})"
    private val volume get() = "Громкость ${(volumeSlider.value * 100).toInt()}%"
    private val soundCheckbox: CheckBox
    private val volumeSlider: Slider
    private val volumeLabel: Label

    init {
        table = scene2d.table {
            row().expandX().fillX().padTop(10f)
            volumeSlider = slider(style = "default-horizontal") { cell ->
                cell.width(WorldDimens.WIDTH * WorldDimens.CELL_SIZE * 0.5f)
                value = game.preferences["music_volume", 0.5f]
                onChange {
                    game.preferences["music_volume"] = value
                    game.audioService.changeVolume(value)
                    setVolumeLabel()
                }
            }
            volumeLabel = label("", "secondary") { cell ->
                cell.padLeft(20f)
            }
            row().expandX().fillX().colspan(2).padTop(20f)
            soundCheckbox = checkBox("", "checkBox") {
                padTop(10f)
                isChecked = game.preferences["audio_on", true]
                label.setAlignment(Align.center)
                onChange {
                    game.preferences["audio_on"] = isChecked
                    game.setAudio(isChecked)
                    setSoundLabel()
                }
            }
            setSoundLabel()
            setVolumeLabel()
            pack()
        }
    }

    private fun setSoundLabel() = soundCheckbox.label.setText(soundLabel)

    private fun setVolumeLabel() = volumeLabel.setText(volume)
}