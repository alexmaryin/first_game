package ru.alexmaryin.deter_rev.values

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

    // 6..10 enemies under attack
    ENEMY1_UNDER_ATTACK("female1_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),
    ENEMY2_UNDER_ATTACK("female2_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),
    ENEMY3_UNDER_ATTACK("female3_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),
    ENEMY4_UNDER_ATTACK("male1_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),
    ENEMY5_UNDER_ATTACK("male2_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),
    ENEMY6_UNDER_ATTACK("male3_act", playMode = Animation.PlayMode.LOOP_PINGPONG, speedRate = 0.75f),

    // 11..12 cops animation
    COP_WALK_FROM_LEFT("cop_walk", speedRate = 0.6f),
    COP_ATTACK("cop_attack", speedRate = 0.75f),

    NONE("")
}