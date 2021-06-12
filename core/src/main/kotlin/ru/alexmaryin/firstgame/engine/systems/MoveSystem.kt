package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import ktx.ashley.exclude
import ru.alexmaryin.firstgame.Gameplay
import ru.alexmaryin.firstgame.WorldDimens
import ru.alexmaryin.firstgame.engine.components.*
import kotlin.math.max
import kotlin.math.min

class MoveSystem : IteratingSystem(
    allOf(
        TransformComponent::class,
        MoveComponent::class
    ).exclude(RemoveComponent::class).get()) {

    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime
        while (accumulator >= Gameplay.UPDATE_RATE) {
            accumulator -= Gameplay.UPDATE_RATE
            entities.forEach { entity ->
                entity.transform.oldPosition.set(entity.transform.position)
            }
            super.update(Gameplay.UPDATE_RATE)
        }

        val alpha = accumulator / Gameplay.UPDATE_RATE
        entities.forEach { entity ->
            entity.transform.interpolatedPosition.set(
                MathUtils.lerp(entity.transform.oldPosition.x, entity.transform.position.x, alpha),
                MathUtils.lerp(entity.transform.oldPosition.y, entity.transform.position.y, alpha),
                entity.transform.position.z
            )
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val move = entity.move
        val player = entity.player
        player?.let {
            movePlayer(transform, move, it, entity.facing, deltaTime)
        } ?: moveEntity(transform, move, deltaTime)
    }

    private fun movePlayer(transform: TransformComponent, move: MoveComponent, it: PlayerComponent, facing: FacingComponent, deltaTime: Float) {
        move.speed.x = when (facing.direction) {
            FacingDirection.LEFT -> min(0f, move.speed.x - Gameplay.HORIZONTAL_ACCELERATE * deltaTime)
            FacingDirection.RIGHT -> max(0f, move.speed.x + Gameplay.HORIZONTAL_ACCELERATE * deltaTime)
            else -> 0f
        }.also { MathUtils.clamp(it, -Gameplay.MAX_HORIZONTAL_SPEED, Gameplay.MAX_HORIZONTAL_SPEED) }

        move.speed.y = when (facing.direction) {
            FacingDirection.UP -> max(0f, move.speed.y + Gameplay.VERTICAL_ACCELERATE * deltaTime)
            FacingDirection.DOWN -> min(0f, move.speed.y - Gameplay.HORIZONTAL_ACCELERATE * deltaTime)
            else -> 0f
        }.also { MathUtils.clamp(it, -Gameplay.MAX_VERTICAL_SPEED, Gameplay.MAX_VERTICAL_SPEED) }

        moveEntity(transform, move, deltaTime)
    }

    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        transform.position.x = MathUtils.clamp(
            transform.position.x + move.speed.x * deltaTime, 0f, WorldDimens.F_WIDTH - transform.size.x
        )
        transform.position.y = MathUtils.clamp(
            transform.position.y + move.speed.y * deltaTime, 0f, WorldDimens.F_HEIGHT - transform.size.y
        )
    }
}