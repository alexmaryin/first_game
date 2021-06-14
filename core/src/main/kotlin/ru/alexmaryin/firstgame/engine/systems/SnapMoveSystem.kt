package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import ktx.ashley.exclude
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.utils.addClamp
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class SnapMoveSystem : IteratingSystem(
    allOf(
        TransformComponent::class,
        MoveComponent::class,
        FacingComponent::class
    ).exclude(RemoveComponent::class).get()) {

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
                    MathUtils.lerp(oldPosition.x, position.x, alpha * entity.move.speedRatio),
                    MathUtils.lerp(oldPosition.y, position.y, alpha * entity.move.speedRatio),
                    position.z
                )
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        moveEntity(entity.transform, entity.move, entity.facing)
    }

    private fun moveEntity(transform: TransformComponent, move: MoveComponent,facing: FacingComponent?) {
        if (move.isNotMoving) {
            facing?.direction = FacingDirection.STOP
        } else {
            transform.position.addClamp(move.direction, WorldDimens.MIN_BORDER_VECTOR, WorldDimens.maxBorderVector(transform.size))
            facing?.direction = when(move.direction) {
                Move.Up -> FacingDirection.UP
                Move.Down -> FacingDirection.DOWN
                Move.Left, Move.SlowLeft -> FacingDirection.LEFT
                Move.Right, Move.SlowRight -> FacingDirection.RIGHT
                else -> FacingDirection.STOP
            }
            move.isNotMoving = true
        }
    }
}
