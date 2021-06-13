package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

object WorldDimens {
    const val CELL_SIZE = 80
    const val F_CELL_SIZE = 80f
    const val WIDTH = 16
    const val F_WIDTH = 16f
    const val HEIGHT = 9
    const val F_HEIGHT = 9f
    private const val F_MAX_DEPTH = 99f
    const val ROADS_OFFSET_Y = -1f

    val MIN_BORDER_VECTOR = Vector3(0f, 0f, 0f)
    fun maxBorderVector(size: Vector2) = Vector3(F_WIDTH - size.x, F_HEIGHT - size.y, F_MAX_DEPTH)
}