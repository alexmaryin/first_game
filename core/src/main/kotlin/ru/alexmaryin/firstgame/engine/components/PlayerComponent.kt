package ru.alexmaryin.firstgame.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.values.Gameplay
import kotlin.math.max
import kotlin.math.min

class PlayerComponent : Component, Pool.Poolable, GameEventsListener {

    var enemiesCaught = 0
    var missedEnemies = 0
    private var missedCops = 0
    var availableCops = Gameplay.MAX_AVAILABLE_COPS

    init { reset() }

    override fun reset() {
        enemiesCaught = 0
        missedEnemies = 0
        missedCops = 0
        availableCops = Gameplay.MAX_AVAILABLE_COPS

        EventDispatcher.subscribeOn<CopMissed>(this)
        EventDispatcher.subscribeOn<EnemyMissed>(this)
        EventDispatcher.subscribeOn<EnemyCaught>(this)
    }

    fun restoreCop() {
        availableCops = min(availableCops + 1, Gameplay.MAX_AVAILABLE_COPS)
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    override fun onEventDelivered(event: GameEvent) {
        when (event) {
            is CopMissed -> {availableCops--; missedCops++ }
            is EnemyCaught -> enemiesCaught++
            is EnemyMissed -> missedEnemies++

            else -> {}
        }
        if (max(missedCops, missedEnemies) >= Gameplay.MAX_MISSED_ENEMIES)
            EventDispatcher.send(GameOver(enemiesCaught))
    }
}

val Entity.player get() = this[PlayerComponent.mapper] ?: throw NullPointerException("Entity $this has no Player component")