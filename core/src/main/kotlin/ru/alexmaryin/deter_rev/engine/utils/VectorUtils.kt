package ru.alexmaryin.deter_rev.engine.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ru.alexmaryin.deter_rev.values.WorldDimens


fun Vector3.addClamp(delta: Vector3, size: Vector2) {
    set(
        MathUtils.clamp(x + delta.x, 0f, WorldDimens.WIDTH + 1f - size.x),
        MathUtils.clamp(y + delta.y, 0f, WorldDimens.HEIGHT + 1f - size.y),
        z + delta.z
    )
}