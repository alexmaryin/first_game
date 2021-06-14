package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.graphics.g2d.Animation

enum class AnimationType(
    val atlasKey: String,
    val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
    var speedRate: Float = 1f
) {
    ENEMY1_WALK_FROM_LEFT("female_walk", speedRate = 0.4f),
    ENEMY2_WALK_FROM_LEFT("male_walk", speedRate = 0.4f),
    NONE("")
}