package ru.alexmaryin.deter_rev.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ru.alexmaryin.deter_rev.engine.components.RemoveComponent
import ru.alexmaryin.deter_rev.engine.components.remove

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