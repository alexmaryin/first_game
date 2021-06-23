package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.deter_rev.values.Move
import ru.alexmaryin.deter_rev.values.Stand

class MoveComponent : Component, Pool.Poolable {

    var isNotMoving = true
    var direction: Move = Stand
    var initialSpeed = 0f

    override fun reset() {
        isNotMoving = true
        direction = Stand
        initialSpeed = 0f
    }

    fun moveToPosition(delta: Move) {
        isNotMoving = false
        direction = delta
    }

    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}

val Entity.move get() = this[MoveComponent.mapper] ?: throw NullPointerException("Entity $this has no Move component")