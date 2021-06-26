package ru.alexmaryin.deter_rev.engine.utils

import com.badlogic.gdx.Preferences
import ktx.preferences.get
import ktx.preferences.set

fun Preferences.scoresList(key: String) = this[key, ""].split("\n").filterNot { it.isBlank() }
    .map { it.substringAfter("#score#").toInt() to it.substringBefore("#score#") }
    .sortedByDescending { it.first }.toMutableList()

fun Preferences.toScoresLine(key: String, list: List<Pair<Int, String>>) {
    this[key] = list.take(10).joinToString("\n") { "${it.second}#score#${it.first}" }
}