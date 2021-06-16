package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

enum class CopState {
    WALK_TO_ENEMY,
    ATTACK,
    WALK_BACK
}
class CopComponent : Component, Pool.Poolable {

    var state = CopState.WALK_TO_ENEMY
    var attackTime = 0f

    override fun reset() {
        state = CopState.WALK_TO_ENEMY
        attackTime = 0f
    }

    companion object {
        val mapper = mapperFor<CopComponent>()
    }
}

val Entity.cop get() = this[CopComponent.mapper] ?: throw NullPointerException("Entity $this has no Cop component")