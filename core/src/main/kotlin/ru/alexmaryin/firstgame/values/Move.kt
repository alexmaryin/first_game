package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.math.Vector3

sealed class Move { abstract val vector: Vector3 }

object Stand : Move() { override val vector: Vector3 get() = Vector3.Zero }
object MoveUp : Move() { override val vector get() = Vector3(0f, 1f, 0f) }
object MoveDown : Move() { override val vector get() = Vector3(0f, -1f, 0f) }
class MoveLeft(private val level: Int = -1) : Move() {
    override val vector get() = Vector3(if (level > 0) (-0.25f - level / 20f) else -1f, 0f, 0f)
}
class MoveRight(private val level: Int = -1) : Move() {
    override val vector get() = Vector3(if (level > 0) (0.25f + level / 20f) else 1f, 0f, 0f)
}

object RotationDeg {
    const val ZERO = 0f
    const val UP = 0f
    const val DOWN = 180f
    const val RIGHT = 270f
    const val LEFT = 90f
}