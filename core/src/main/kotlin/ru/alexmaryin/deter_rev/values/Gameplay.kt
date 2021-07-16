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
    private const val ENEMY_ARISE_MIN_INTERVAL = 3f  // interval before new enemy might arise in seconds
    private const val ENEMY_ATTACK_MIN_INTERVAL = 5f
    private const val ENEMY_ATTACK_MAX_INTERVAL = 10f
    const val DIFFICULTY_RATIO = 1f // multiplier or divider for difficulty ratio
    const val LEVEL_UP = 10f    // count of caught enemies to level up
    const val LEVELS_MAX = 20   // max of levels
    const val CROSS_FADE_DURATION = 3f        // duration in seconds

    fun nextEnemyInterval(level: Int) = 18 / (0.5 * (level - 1) + 6) // interval before new enemy might arise in seconds
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
        "Вакцинировали!",
        "Чипировали!",
    )

    const val CREDITS = "Разработано Alex Maryin (@java73), 2021,\nс использованием библиотек libGdx, scene2d на Kotlin.\n" +
            "Распространяется под лицензией Apache 2.0\n\n" +
            "Спрайты взяты с сайтов craftpix.net, itch.io\n" +
            "Звуки - Kenney с OpenGameArt.org\n" +
            "Музыка - из конкурсов Ludum Dare 28, 30, 32\n\n" +
            "Разработчик просит с иронией и юмором относиться к творящемуся в игре сарказму, " +
            "поскольку только так и можно пережить смутные диктаторские времена. В игре нельзя выиграть, " +
            "как и в жизни любой диктатор рано или поздно сойдет со страниц истории, оставив память лишь о своих зверствах, " +
            "жадности и одержимости властью."
}