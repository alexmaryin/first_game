package ru.alexmaryin.deter_rev.engine.events

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.GdxSet
import ktx.collections.set
import kotlin.reflect.KClass

object EventDispatcher {
    val listeners = ObjectMap<KClass<out GameEvent>, GdxSet<GameEventsListener>>()

    inline fun <reified T : GameEvent> subscribeOn(listener: GameEventsListener) {
        if (!listeners.containsKey(T::class))
            listeners[T::class] = GdxSet()
        listeners[T::class].add(listener)
    }

    fun removeSubscribes(listener: GameEventsListener) {
        ObjectMap.Keys(listeners).forEach { event ->
            listeners[event].remove(listener)
            if (listeners[event].isEmpty)
                listeners.remove(event)
        }
    }

    inline fun <reified T : GameEvent> removeSubscribe(listener: GameEventsListener) {
        listeners[T::class].remove(listener)
        if (listeners[T::class].isEmpty)
            listeners.remove(T::class)
    }

    fun send(event: GameEvent) {
        if (listeners.containsKey(event::class)) {
            listeners[event::class].forEach { listener ->
                listener.onEventDelivered(event)
            }
        }
    }
}

