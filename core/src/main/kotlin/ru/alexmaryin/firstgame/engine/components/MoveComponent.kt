package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class MoveComponent : Component, Pool.Poolable {

    val speed = Vector2()
    var isNotMoving = true
    val direction = Vector3()

    override fun reset() {
        speed.set(0f, 0f)
        isNotMoving = true
        direction.set(Vector3.Zero)
    }

    fun moveToPosition(delta: Vector3) {
        isNotMoving = false
        direction.set(delta)
    }

    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}

val Entity.move get() = this[MoveComponent.mapper] ?: throw NullPointerException("Entity $this has no Move component")