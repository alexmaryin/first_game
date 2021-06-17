package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import ktx.ashley.exclude
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.utils.addClamp
import ru.alexmaryin.firstgame.values.*

class SnapMoveSystem : IteratingSystem(
    allOf(MoveComponent::class).exclude(RemoveComponent::class).get()) {

    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while (accumulator >= Gameplay.MOVE_RATE) {
            accumulator -= Gameplay.MOVE_RATE
            entities.forEach { entity ->
                entity.transform.oldPosition.set(entity.transform.position)
            }
            super.update(Gameplay.MOVE_RATE)
        }

        val alpha = accumulator / Gameplay.MOVE_RATE
        entities.forEach { entity ->
            with(entity.transform) {
                interpolatedPosition.set(
                    MathUtils.lerp(oldPosition.x, position.x, alpha),
                    MathUtils.lerp(oldPosition.y, position.y, alpha),
                    position.z
                )
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity.move
        val facing = entity.facing
        val transform = entity.transform

        if (move.isNotMoving) {
            facing?.direction = FacingDirection.STOP
        } else {
            val vector = move.direction.vector
            when(move.direction) {
                is MoveUp -> { facing?.direction = FacingDirection.UP; vector.y += move.initialSpeed }
                is MoveDown -> { facing?.direction = FacingDirection.DOWN; vector.y -= move.initialSpeed }
                is MoveLeft -> { facing?.direction = FacingDirection.LEFT; vector.x -= move.initialSpeed }
                is MoveRight -> { facing?.direction = FacingDirection.RIGHT; vector.x += move.initialSpeed }
                else -> facing?.direction = FacingDirection.STOP
            }
            transform.position.addClamp(vector, transform.size)
            move.isNotMoving = true
        }
    }

    fun stopMoving(vararg entities: Entity) {
        entities.forEach { entity ->
            with (entity.transform) {
                oldPosition.set(interpolatedPosition)
                position.set(interpolatedPosition)
            }
            entity.move.isNotMoving = true
            entity.move.direction = Stand
        }
    }
}
