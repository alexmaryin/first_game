package ru.alexmaryin.deter_rev.values

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
    private const val ENEMY_ARISE_MIN_INTERVAL = 0.5f  // interval before new enemy might arise in seconds
    private const val ENEMY_ATTACK_MIN_INTERVAL = 5f
    private const val ENEMY_ATTACK_MAX_INTERVAL = 10f
    const val DIFFICULTY_RATIO = 1f // multiplier or divider for difficulty ratio
    const val LEVEL_UP = 10f    // count of caught enemies to level up
    const val LEVELS_MAX = 20   // max of levels
    const val CROSS_FADE_DURATION = 3f        // duration in seconds

    val nextEnemyInterval get() = ENEMY_ARISE_MIN_INTERVAL / DIFFICULTY_RATIO
    val nextAttackTime get() = random(ENEMY_ATTACK_MIN_INTERVAL / DIFFICULTY_RATIO, ENEMY_ATTACK_MAX_INTERVAL / DIFFICULTY_RATIO)

    const val INTRO_HELP = "Вы управляете полицейской машиной, в вашем распоряжении десять полицейских. " +
            "Слева по четырем дорожкам к замку вашего любимого диктатора двигаются революционеры, которых вы должны отгонять.\n\n" +
            "Для этого кнопками вверх вниз подгоните машину к нужной дорожке и выпустите полицейского кнопкой пробел. " +
            "После того, как полицейский разберётся с человечком единственным доступным ему методом, он пойдет назад, " +
            "его нужно забрать в машину.\n\nЕсли вы не заберёте полицейского или выпустите лишнего на дорожку без революционера," +
            " он уйдет и не вернётся, скорее всего, примкнув к рядам мятежников.\n\n" +
            "В ваших руках ключи от стабильности, товарищ майор."

    const val INTRO_HELP_MOBILE = "Вы управляете полицейской машиной, в вашем распоряжении десять полицейских. " +
            "Слева по четырем дорожкам к замку вашего любимого диктатора двигаются революционеры, которых вы должны отгонять.\n\n" +
            "Для этого, нажимая выше или ниже, подгоните машину к нужной дорожке и выпустите полицейского, нажав на дорожку. " +
            "После того, как полицейский разберётся с человечком единственным доступным ему методом, он пойдет назад, " +
            "его нужно забрать в машину.\n\nЕсли вы не заберёте полицейского или выпустите лишнего на дорожку без революционера," +
            " он уйдет и не вернётся, скорее всего, примкнув к рядам мятежников.\n\n" +
            "В ваших руках ключи от стабильности, товарищ майор."

    val CATCH_PHRASES = listOf(
        "Оприходовали!",
        "Замочили в сортире!",
        "Скормили бандерлогам!",
        "Побеседовали!",
        "Показали библиотеку!",
    )
}