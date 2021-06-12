package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

const val MAX_FULLNESS = 10
const val MAX_AVAILABLE_COPS = 5

class PlayerComponent : Component, Pool.Poolable {

    var fullness = 0
    var prisonersCount = 0
    var availableCops = MAX_AVAILABLE_COPS

    override fun reset() {
        fullness = 0
        prisonersCount = 0
        availableCops = MAX_AVAILABLE_COPS
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}

val Entity.player get() = this[PlayerComponent.mapper]