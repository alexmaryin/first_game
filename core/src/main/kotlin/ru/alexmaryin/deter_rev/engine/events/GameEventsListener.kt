package ru.alexmaryin.deter_rev.engine.events

interface GameEventsListener {
    fun onEventDelivered(event: GameEvent)
}