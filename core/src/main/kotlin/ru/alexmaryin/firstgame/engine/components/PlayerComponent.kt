package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.firstgame.values.Gameplay.MAX_AVAILABLE_COPS

class PlayerComponent : Component, Pool.Poolable {

    var enemiesCaught = 0
    var missedEnemies = 0
    var availableCops = MAX_AVAILABLE_COPS

    override fun reset() {
        enemiesCaught = 0
        missedEnemies = 0
        availableCops = MAX_AVAILABLE_COPS
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}

val Entity.player get() = this[PlayerComponent.mapper]