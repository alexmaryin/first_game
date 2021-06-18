package ru.alexmaryin.firstgame.values

object Entities {

    // Cop values
    const val COP_WIDTH_SPRITE_RATIO = 0.85f
    const val COP_HEIGHT_SPRITE_RATIO = 0.95f
    const val COP_Y_SPRITE_OFFSET = 10f / WorldDimens.F_CELL_SIZE
    const val COP_X_SPRITE_OFFSET = 0f / WorldDimens.F_CELL_SIZE

    // Enemy values
    const val ENEMY_WIDTH_SPRITE_RATIO = 2f
    const val ENEMY_HEIGHT_SPRITE_RATIO = 2f
    const val ENEMY_Y_SPRITE_OFFSET = -30f / WorldDimens.F_CELL_SIZE
    const val ENEMY_X_SPRITE_OFFSET = 0f / WorldDimens.F_CELL_SIZE

    // Player values
    const val CAR_WIDTH_SPRITE_RATIO = 1.5f
    const val CAR_HEIGHT_SPRITE_RATIO = 1.5f
    const val CAR_Y_SPRITE_OFFSET = -20f / WorldDimens.F_CELL_SIZE
    const val CAR_X_SPRITE_OFFSET = 10f / WorldDimens.F_CELL_SIZE
}