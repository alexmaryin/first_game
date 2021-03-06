package ru.alexmaryin.deter_rev.engine.events

interface GameEventsHandler {
    fun updateEnemiesCaught(count: Int)
    fun updateEnemiesMissed(count: Int)
    fun updateMissedCops(count: Int)
    fun updateAvailableCops(count: Int)
    fun updateLevel(level: Int)
    fun showMessage(message: String, duration: Float = 1f)
}