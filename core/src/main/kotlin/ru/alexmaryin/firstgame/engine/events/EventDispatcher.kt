package ru.alexmaryin.firstgame.engine.events

import kotlin.reflect.KClass

object EventDispatcher {
    val listeners = mutableMapOf<KClass<out GameEvent>, MutableSet<GameEventsListener>>()

    inline fun <reified T : GameEvent> subscribeOn(listener: GameEventsListener) {
        if (listeners.contains(T::class)) {
            listeners[T::class]!!.add(listener)
        } else {
            listeners[T::class] = mutableSetOf(listener)
        }
    }

    fun removeSubscriptions(listener: GameEventsListener) {
        listeners.forEach { (event, set) ->
            set.remove(listener)
            if (set.isEmpty()) {
                listeners.remove(event)
            }
        }
    }

    fun send(event: GameEvent) {
        if (listeners.contains(event::class)) {
            listeners[event::class]!!.forEach { listener ->
                listener.onEventDelivered(event)
            }
        }
    }
}

