package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.collections.GdxArray
import ru.alexmaryin.deter_rev.values.AnimationType
import ru.alexmaryin.deter_rev.values.Gameplay

class Animation2D(
    val type: AnimationType,
    private val keyFrames: GdxArray<out TextureRegion>,
) : Animation<TextureRegion>(Gameplay.DEFAULT_ANIMATION_RATE / type.speedRate, keyFrames, type.playMode)