package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.math.MathUtils.random

object Gameplay {
    const val DEFAULT_TITLE = "Deter revolution"
    const val MOVE_RATE = 1/2f   // frames per move
    const val DEBUG_UPDATE_RATE = 1/4f // interval for debug info updating
    const val DEFAULT_ANIMATION_RATE = 1/25f // default animation rate 25 frames per second
    const val MIN_DELTA_TME = 1/20f     // min FPS for game
    const val MAX_MISSED_ENEMIES = 5    // missed enemies count to game over
    const val GAME_OVER_DELAY = 1f
    const val MAX_AVAILABLE_COPS = 10      // initially available cops count for player
    private const val ENEMY_ARISE_MIN_INTERVAL = 3f  // interval before new enemy might arise in seconds
    private const val ENEMY_ATTACK_MIN_INTERVAL = 5f
    private const val ENEMY_ATTACK_MAX_INTERVAL = 10f
    const val DIFFICULTY_RATIO = 1f // multiplier or divider for difficulty ratio
    const val LEVEL_UP = 10f    // count of caught enemies to level up
    const val LEVELS_MAX = 20   // max of levels

    val nextEnemyInterval get() = ENEMY_ARISE_MIN_INTERVAL / DIFFICULTY_RATIO
    val nextAttackTime get() = random(ENEMY_ATTACK_MIN_INTERVAL / DIFFICULTY_RATIO, ENEMY_ATTACK_MAX_INTERVAL / DIFFICULTY_RATIO)
}