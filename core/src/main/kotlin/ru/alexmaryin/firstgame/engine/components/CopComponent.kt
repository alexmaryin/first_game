package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class CopComponent : Component, Pool.Poolable {

    var isMissed = false

    override fun reset() {
        isMissed = false
    }

    companion object {
        val mapper = mapperFor<CopComponent>()
    }
}

val Entity.cop get() = this[CopComponent.mapper] ?: throw NullPointerException("Entity $this has no Cop component")