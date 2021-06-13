package ru.alexmaryin.firstgame.engine.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Gameplay

typealias GdxArray = Array<out TextureRegion>

class Animation2D(
    val type: AnimationType,
    val keyFrames: GdxArray,
) : Animation<TextureRegion>(Gameplay.DEFAULT_ANIMATION_RATE / type.speedRate, keyFrames, type.playMode)