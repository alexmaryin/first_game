package ru.alexmaryin.deter_rev.engine.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor
import ru.alexmaryin.deter_rev.engine.events.*
import ru.alexmaryin.deter_rev.engine.events.GameEventsHandler
import ru.alexmaryin.deter_rev.values.Gameplay

class PlayerComponent : Component, Pool.Poolable, GameEventsListener {

    var enemiesCaught = 0
    var missedEnemies = 0
    private var missedCops = 0
    var availableCops = Gameplay.MAX_AVAILABLE_COPS
    var uiHandler: GameEventsHandler? = null

    init { reset() }

    override fun reset() {
        enemiesCaught = 0
        missedEnemies = 0
        missedCops = 0
        availableCops = Gameplay.MAX_AVAILABLE_COPS

        EventDispatcher.subscribeOn<CopMissed>(this)
        EventDispatcher.subscribeOn<EnemyMissed>(this)
        EventDispatcher.subscribeOn<EnemyCaught>(this)
        EventDispatcher.subscribeOn<PlayerSendCop>(this)
        EventDispatcher.subscribeOn<PlayerRestoresCop>(this)
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

    override fun onEventDelivered(event: GameEvent) {
        when (event) {
            is PlayerSendCop -> {
                availableCops--
                availableCops.coerceAtLeast(0)
                uiHandler?.updateAvailableCops(availableCops)
            }
            is PlayerRestoresCop -> {
                availableCops++
                availableCops.coerceAtMost(Gameplay.MAX_AVAILABLE_COPS)
                uiHandler?.updateAvailableCops(availableCops)
            }
            is CopMissed -> {
                missedCops++
                uiHandler?.updateMissedCops(missedCops)
            }
            is EnemyCaught -> {
                enemiesCaught++
                uiHandler?.updateEnemiesCaught(enemiesCaught)
            }
            is EnemyMissed -> {
                missedEnemies++
                uiHandler?.updateEnemiesMissed(missedEnemies)
            }

            else -> {}
        }
        if (missedEnemies >= Gameplay.MAX_MISSED_ENEMIES || missedCops == Gameplay.MAX_AVAILABLE_COPS)
            EventDispatcher.send(GameOver(enemiesCaught))
    }
}

val Entity.player get() = this[PlayerComponent.mapper] ?: throw NullPointerException("Entity $this has no Player component")