package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.Move

class PlayerInputSystem(
    private val viewport: Viewport
) : IteratingSystem(allOf(
    PlayerComponent::class,
    TransformComponent::class,
    FacingComponent::class
).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity.move
        if (move.isNotMoving) when {
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> move.moveToPosition(Move.Up)
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> move.moveToPosition(Move.Down)
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> move.moveToPosition(Move.Left)
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> move.moveToPosition(Move.Right)
        }
    }
}