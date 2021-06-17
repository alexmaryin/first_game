package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.PlayerComponent
import ru.alexmaryin.firstgame.engine.components.move
import ru.alexmaryin.firstgame.engine.components.player
import ru.alexmaryin.firstgame.engine.components.transform
import ru.alexmaryin.firstgame.values.MoveDown
import ru.alexmaryin.firstgame.values.MoveUp
import ru.alexmaryin.firstgame.values.WorldDimens
import kotlin.math.round

class PlayerInputSystem : IteratingSystem(allOf(PlayerComponent::class).get()) {

    private val log = logger<PlayerInputSystem>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity.move
        if (move.isNotMoving) {
            val position = entity.transform.position
            when {
                Gdx.input.isKeyJustPressed(Input.Keys.UP) && position.y < WorldDimens.F_HEIGHT - 2f -> move.moveToPosition(MoveUp())
                Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && position.y > 1f -> move.moveToPosition(MoveDown())
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> {
                    val player = entity.player
                    if (player.availableCops > 0 &&  round(position.y) in WorldDimens.ROADS_Y_CELLS) {
                        engine.getSystem<CopSystem>().addCop(round(position.y))
                        log.debug { "Add cop at road ${round(position.y)}" }
                        player.availableCops -= 1
                    } else {
                        log.debug { "No available cops!" }
                    }
                }
            }
        }
    }
}