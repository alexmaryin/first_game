package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.graphics.g2d.Animation

enum class AnimationType(
    val atlasKey: String,
    val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
    var speedRate: Float = 1f
) {
    WALK_FROM_LEFT("female_walk"),
    NONE("")
}