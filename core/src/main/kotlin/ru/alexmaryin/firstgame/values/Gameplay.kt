package ru.alexmaryin.firstgame.values

object Gameplay {
    const val DEFAULT_TITLE = "Deter revolution"
    const val MOVE_RATE = 1/2f   // frames per move
    const val DEBUG_UPDATE_RATE = 1/4f // interval for debug info updating
    const val DEFAULT_ANIMATION_RATE = 1/25f
    const val MIN_DELTA_TME = 1/20f
    const val MAX_MISSED_ENEMIES = 5
    const val GAME_OVER_DELAY = 1f
    const val MAX_AVAILABLE_COPS = 10
    const val ENEMY_ARISE_MIN_INTERVAL = 3f  // interval before new enemy might arise in seconds
    const val DIFFICULTY_RATIO = 1f // multiplier for max of enemies on screen, speed or other
}