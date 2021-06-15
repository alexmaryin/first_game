package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.Move

class PlayerInputSystem : IteratingSystem(allOf(
    PlayerComponent::class,
    TransformComponent::class,
    FacingComponent::class
).get()) {

    private val log = logger<PlayerInputSystem>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity.move
        if (move.isNotMoving) when {
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> move.moveToPosition(Move.Up)
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> move.moveToPosition(Move.Down)
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> move.moveToPosition(Move.Left)
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> move.moveToPosition(Move.Right)
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> {
                val player = entity.player
                val position = entity.transform.position
                if (player.availableCops > 0) {
                    engine.getSystem<CopSystem>().addCop(Vector2(position.x, position.y))
                    player.availableCops -= 1
                } else {
                    log.debug { "No available cops!" }
                }
            }
        }
    }
}