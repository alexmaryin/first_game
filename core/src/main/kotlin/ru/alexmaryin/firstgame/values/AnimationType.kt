package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.graphics.g2d.Animation

enum class AnimationType(
    val atlasKey: String,
    val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
    var speedRate: Float = 1f
) {
    // 0..5 enemies walk from left to right
    ENEMY1_WALK_FROM_LEFT("female1", speedRate = 0.4f),
    ENEMY2_WALK_FROM_LEFT("female2", speedRate = 0.4f),
    ENEMY3_WALK_FROM_LEFT("female3", speedRate = 0.4f),
    ENEMY4_WALK_FROM_LEFT("male1", speedRate = 0.4f),
    ENEMY5_WALK_FROM_LEFT("male2", speedRate = 0.4f),
    ENEMY6_WALK_FROM_LEFT("male3", speedRate = 0.4f),

    // 6..7 cops animation
    COP_WALK_FROM_LEFT("cop_walk", speedRate = 0.6f),
    COP_ATTACK("cop_attack", speedRate = 1f),

    NONE("")
}