package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class RemoveComponent : Component, Pool.Poolable {

    var delay = 0f

    override fun reset() { delay = 0f }

    companion object {
        val mapper = mapperFor<RemoveComponent>()
    }
}

val Entity.remove get() = this[RemoveComponent.mapper] ?: throw NullPointerException("Entity $this has no Remove component")