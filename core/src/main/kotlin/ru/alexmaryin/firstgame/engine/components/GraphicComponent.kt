package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class GraphicComponent : Component, Pool.Poolable {

    val sprite = Sprite()
    var flipHorizontal = false
    var flipVertical = false

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
        flipHorizontal = false
        flipVertical = false
    }

    fun setSpriteRegion(region: TextureRegion, rotation: Float = 0f) {
        sprite.apply {
            setOriginCenter()
            setRotation(rotation)
            setRegion(region)
            setFlip(flipHorizontal, flipVertical)
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}

val Entity.graphic get() = this[GraphicComponent.mapper] ?: throw NullPointerException("Entity $this has no Graphic component")