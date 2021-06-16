package ru.alexmaryin.firstgame.engine.events

interface GameEventsListener {
    fun onEventDelivered(event: GameEvent)
}