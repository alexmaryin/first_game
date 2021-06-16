package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ru.alexmaryin.firstgame.engine.components.RemoveComponent
import ru.alexmaryin.firstgame.engine.components.remove
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.engine.events.GameEventsListener

class RemoveSystem : IteratingSystem(
    allOf(RemoveComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.remove.apply {
            delay -= deltaTime
            if (delay <= 0f) {
                engine.removeEntity(entity)
            }
        }
    }
}