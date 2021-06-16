package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

enum class EnemyState {
    WALK_STRAIGHT,
    UNDER_ATTACK,
    WALK_BACK
}

class EnemyComponent : Component, Pool.Poolable {

    var speedRatio = 1f     // should raise for difficulty growing
    var enemyVariant = random(0, 5)    // variant of enemy.
    var road = random(1, 4)
    var state = EnemyState.WALK_STRAIGHT
    var underAttackTime = 0f

    override fun reset() {
        speedRatio = 1f
        road = random(1, 4)
        enemyVariant = random(0, 5)
        state = EnemyState.WALK_STRAIGHT
        underAttackTime = 0f
    }

    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }
}

val Entity.enemy get() = this[EnemyComponent.mapper] ?: throw NullPointerException("Entity $this has no Enemy component")