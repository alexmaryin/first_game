package ru.alexmaryin.firstgame.engine.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.collections.GdxArray
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Gameplay

class Animation2D(
    val type: AnimationType,
    private val keyFrames: GdxArray<out TextureRegion>,
) : Animation<TextureRegion>(Gameplay.DEFAULT_ANIMATION_RATE / type.speedRate, keyFrames, type.playMode)