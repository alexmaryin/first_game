package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.firstgame.values.WorldDimens

enum class EnemyState {
    WALK_STRAIGHT,
    UNDER_ATTACK,
    WALK_BACK
}

class EnemyComponent : Component, Pool.Poolable {

    var enemyVariant = random(0, 5)    // variant of enemy.
    var road = WorldDimens.ROADS_Y_CELLS.random()
    var state = EnemyState.WALK_STRAIGHT
    var underAttackTime = 0f

    override fun reset() {
        road = WorldDimens.ROADS_Y_CELLS.random()
        enemyVariant = random(0, 5)
        state = EnemyState.WALK_STRAIGHT
        underAttackTime = 0f
    }

    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }
}

val Entity.enemy get() = this[EnemyComponent.mapper] ?: throw NullPointerException("Entity $this has no Enemy component")