package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class EnemyComponent : Component, Pool.Poolable {

    var speedRatio = 1f     // should raise for difficulty growing
    var enemyVariant = 0    // variant of enemy. TODO: When other will be implemented - change it to randomize
    var road = random(1, 4)
    var finished = false
    var caught = false

    override fun reset() {
        speedRatio = 1f
        road = random(1, 4)
        enemyVariant = 0
        finished = false
        caught = false
    }

    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }
}

val Entity.enemy get() = this[EnemyComponent.mapper] ?: throw NullPointerException("Entity $this has no Enemy component")