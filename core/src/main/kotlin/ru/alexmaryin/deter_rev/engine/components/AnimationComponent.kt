package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.deter_rev.values.AnimationType

class AnimationComponent : Component, Pool.Poolable {

    private var previousType = AnimationType.NONE
    var type = AnimationType.NONE
    var stateTime = 0f
    lateinit var animation: Animation2D

    override fun reset() {
        type = AnimationType.NONE
        previousType = AnimationType.NONE
        stateTime = 0f
    }

    fun animateEnemyUnderAttack() {
        previousType = type
        type = AnimationType.values()[type.ordinal + 6]
    }

    fun animateCopAttack() {
        previousType = type
        type = AnimationType.COP_ATTACK
    }

    fun setPreviousAnimation() {
        type = previousType
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}

val Entity.animation get() = this[AnimationComponent.mapper] ?: throw NullPointerException("Entity $this has no Animation component")