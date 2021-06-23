package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class FacingComponent : Component, Pool.Poolable {

    var direction = FacingDirection.DEFAULT

    override fun reset() {
        direction = FacingDirection.DEFAULT
    }

    companion object {
        val mapper = mapperFor<FacingComponent>()
    }
}

val Entity.facing get() = this[FacingComponent.mapper]

enum class FacingDirection {
    LEFT, UP, DEFAULT, RIGHT, DOWN, STOP
}