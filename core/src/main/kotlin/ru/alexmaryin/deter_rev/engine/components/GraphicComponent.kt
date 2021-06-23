package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class GraphicComponent : Component, Pool.Poolable {

    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    fun setSpriteRegion(region: TextureRegion, rotation: Float = 0f) {
        sprite.apply {
            setOriginCenter()
            setRotation(rotation)
            setRegion(region)
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}

val Entity.graphic get() = this[GraphicComponent.mapper] ?: throw NullPointerException("Entity $this has no Graphic component")