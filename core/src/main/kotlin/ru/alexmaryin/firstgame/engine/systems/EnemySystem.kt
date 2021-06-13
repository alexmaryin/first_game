package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class EnemySystem : IteratingSystem(
    allOf(TransformComponent::class, MoveComponent::class)
        .exclude(PlayerComponent::class, RemoveComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.move.moveToPosition(Move.SlowRight)
        if (entity.transform.position.x >= WorldDimens.F_WIDTH - 1) {
            entity.addComponent<RemoveComponent>(engine) { delay = 0.5f }
        }
    }
}