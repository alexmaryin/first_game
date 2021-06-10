package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ru.alexmaryin.firstgame.engine.components.GraphicComponent
import ru.alexmaryin.firstgame.engine.components.TransformComponent
import ru.alexmaryin.firstgame.engine.components.transform

class RenderSystem : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
    }
}