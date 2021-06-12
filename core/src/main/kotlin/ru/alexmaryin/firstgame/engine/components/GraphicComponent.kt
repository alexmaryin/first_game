package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.firstgame.WorldDimens

class GraphicComponent : Component, Pool.Poolable {

    val sprite = Sprite()

    override fun reset() {
        sprite.texture = null
        sprite.setColor(1f, 1f, 1f, 1f)
    }

    fun setSpriteRegion(region: TextureRegion) {
        sprite.run {
            setRegion(region)
            setSize(region.regionWidth / WorldDimens.F_CELL_SIZE, region.regionHeight / WorldDimens.F_CELL_SIZE)
            setOriginCenter()
        }
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}

val Entity.graphic get() = this[GraphicComponent.mapper] ?: throw NullPointerException("Entity $this has no Graphic component")