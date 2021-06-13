package ru.alexmaryin.firstgame.engine.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3


fun Vector3.addClamp(delta: Vector3, min: Vector3, max: Vector3) {
    set(
        MathUtils.clamp(x + delta.x, min.x, max.x),
        MathUtils.clamp(y + delta.y, min.y, max.y),
        MathUtils.clamp(z + delta.z, min.z, max.z),
    )
}