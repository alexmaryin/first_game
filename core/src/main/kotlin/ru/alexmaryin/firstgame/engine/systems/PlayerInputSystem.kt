package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ru.alexmaryin.firstgame.engine.components.*

const val TOUCH_TOLERANCE_DISTANCE = 0.5f

class PlayerInputSystem(
    private val viewport: Viewport
) : IteratingSystem(allOf(
    PlayerComponent::class,
    TransformComponent::class,
    FacingComponent::class
).get()) {

    private val posVector = Vector2(0f, 0f)

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val facing = entity.facing
        val transform = entity.transform
        posVector.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        viewport.unproject(posVector)
        val diffX = posVector.x - transform.position.x - transform.size.x * 0.5f
        val diffY = posVector.y - transform.position.y - transform.size.y * 0.5f
        facing.direction = when {
            diffY > TOUCH_TOLERANCE_DISTANCE -> FacingDirection.UP
            diffY < -TOUCH_TOLERANCE_DISTANCE -> FacingDirection.DOWN
            diffX < -TOUCH_TOLERANCE_DISTANCE -> FacingDirection.LEFT
            diffX > TOUCH_TOLERANCE_DISTANCE -> FacingDirection.RIGHT
            else -> FacingDirection.DEFAULT
        }

    }
}