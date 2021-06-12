package ru.alexmaryin.firstgame

object Gameplay {
    const val DEFAULT_TITLE = "Deter revolution"
    const val HORIZONTAL_ACCELERATE = 16.5f
    const val VERTICAL_ACCELERATE = 16.5f
    const val MAX_HORIZONTAL_SPEED = 5.5f
    const val MAX_VERTICAL_SPEED = 5.5f
    const val UPDATE_RATE = 0.04f   // 25 frames per second
    const val DEBUG_UPDATE_RATE = 0.25f // interval for debug info updating
    const val MIN_DELTA_TME = 0.05f
    const val MAX_MISSED_ENEMIES = 5
    const val GAME_OVER_DELAY = 1f
}