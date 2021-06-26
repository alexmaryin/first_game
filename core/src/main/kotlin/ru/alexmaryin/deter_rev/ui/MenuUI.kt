package ru.alexmaryin.deter_rev.ui

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*
import ru.alexmaryin.deter_rev.values.Gameplay

class MenuUI() {
    val table: KTableWidget
    val newGameButton: TextButton
    val settingsButton: TextButton
    val quitButton: TextButton
    private val pane: ScrollPane

    init {
        table = scene2d.table {
            setFillParent(true)

            row().colspan(2).expandX().fillX().pad(10f)
            label(Gameplay.DEFAULT_TITLE, "red") {
                setAlignment(Align.center)
                setFontScale(3f)
                wrap = true
            }

            row().expandY()
            table {
                row().expandX().fillX().pad(10f)
                newGameButton = textButton("Новая игра", "green_button") {
                    label.setFontScale(2f)
                    label.wrap = true
                }
                row().expandX().fillX().pad(10f)
                settingsButton = textButton("Настройки", "yellow_button") {
                    label.setFontScale(2f)
                    label.wrap = true
                }
                row().expandX().fillX().pad(10f)
                quitButton = textButton("Выход", "red_button") {
                    label.setFontScale(2f)
                    label.wrap = true
                }
                pack()
            }

            pane = scrollPane("default_scroll")

            row().colspan(2).expandX().pad(10f)
            label("© Alex Maryin (@java73), 2021. Powered by libGdx, scene2d with Kotlin.")

            pack()
        }
    }

    private fun updateScoresTable(scoresList: MutableList<Pair<Int, String>>) = scene2d.table {
        row().colspan(2).expandX().pad(5f)
        label("Таблица рекордов:", "default_bordered") { cell ->
            cell.top().left().expandX().fillX()
            setFontScale(2f)
        }
        scoresList.forEachIndexed { index, (score, name) ->
            row().expandX().pad(5f)
            label("${index + 1}. $name", "default") { cell -> cell.left().expandX() }
            label(score.toString(), "default") { cell -> cell.right(); setFontScale(1.5f) }
        }
        pack()
    }

    fun updateScores(scoresList: MutableList<Pair<Int, String>>) {
        pane.actor = updateScoresTable(scoresList)
    }
}