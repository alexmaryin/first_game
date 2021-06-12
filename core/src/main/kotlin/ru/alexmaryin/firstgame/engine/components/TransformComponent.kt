package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {

    val position = Vector3()
    val oldPosition = Vector3()
    val interpolatedPosition = Vector3()
    val size = Vector2(1f, 1f)
    var rotation = 0f

    override fun reset() {
        position.set(Vector3.Zero)
        oldPosition.set(Vector3.Zero)
        interpolatedPosition.set(Vector3.Zero)
        size.set(1f, 1f)
        rotation = 0f
    }

    fun setInitialPosition(x: Float, y: Float, z: Float = 0f) {
        position.set(x, y, z)
        oldPosition.set(x, y, z)
        interpolatedPosition.set(x, y, z)
    }
    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if(zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}

val Entity.transform get() = this[TransformComponent.mapper] ?: throw NullPointerException("Entity $this has no Transform component")