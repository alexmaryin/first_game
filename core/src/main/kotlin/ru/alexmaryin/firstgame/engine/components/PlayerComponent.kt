package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.Gameplay.MAX_AVAILABLE_COPS
import kotlin.math.max
import kotlin.math.min

class PlayerComponent : Component, Pool.Poolable, GameEventsListener {

    var enemiesCaught = 0
    var missedEnemies = 0
    var availableCops = MAX_AVAILABLE_COPS

    init { reset() }

    override fun reset() {
        enemiesCaught = 0
        missedEnemies = 0
        availableCops = MAX_AVAILABLE_COPS

        EventDispatcher.subscribeOn<CopMissed>(this)
        EventDispatcher.subscribeOn<EnemyMissed>(this)
        EventDispatcher.subscribeOn<EnemyCaught>(this)
    }

    fun restoreCop() {
        availableCops = min(availableCops + 1, MAX_AVAILABLE_COPS)
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    override fun onEventDelivered(event: GameEvent) {
        when (event) {
            is CopMissed -> availableCops = max(availableCops - 1, 0)
            is EnemyCaught -> enemiesCaught++
            is EnemyMissed -> {
                missedEnemies++
                if (missedEnemies >= Gameplay.MAX_MISSED_ENEMIES)
                    EventDispatcher.send(GameOver(enemiesCaught))
            }
            else -> {}
        }
    }
}

val Entity.player get() = this[PlayerComponent.mapper] ?: throw NullPointerException("Entity $this has no Player component")