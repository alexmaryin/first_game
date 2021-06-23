package ru.alexmaryin.deter_rev.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.deter_rev.engine.components.PlayerComponent
import ru.alexmaryin.deter_rev.engine.components.move
import ru.alexmaryin.deter_rev.engine.components.player
import ru.alexmaryin.deter_rev.engine.components.transform
import ru.alexmaryin.deter_rev.engine.events.EventDispatcher
import ru.alexmaryin.deter_rev.engine.events.PlayerSendCop
import ru.alexmaryin.deter_rev.values.MoveDown
import ru.alexmaryin.deter_rev.values.MoveUp
import ru.alexmaryin.deter_rev.values.WorldDimens
import kotlin.math.round

enum class PlayerAction { UP, DOWN, COP, NONE }

class PlayerInputSystem(
    private val viewport: Viewport
) : IteratingSystem(allOf(PlayerComponent::class).get()) {

    private val log = logger<PlayerInputSystem>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity.move
        if (move.isNotMoving) {
            val position = entity.transform.position
            val action = when (Gdx.app.type) {
                Application.ApplicationType.Desktop -> checkKeyboardInput()
                Application.ApplicationType.Android -> checkTouchInput(position)
                else -> PlayerAction.NONE
            }
            if (action == PlayerAction.NONE)
                return
            when {
                action == PlayerAction.UP && position.y < WorldDimens.HEIGHT - 3f -> move.moveToPosition(MoveUp())
                action == PlayerAction.DOWN && position.y > 1f -> move.moveToPosition(MoveDown())
                action == PlayerAction.COP -> {
                    val player = entity.player
                    if (player.availableCops > 0 && round(position.y) in WorldDimens.ROADS_Y_CELLS) {
                        engine.getSystem<CopSystem>().addCop(round(position.y))
                        EventDispatcher.send(PlayerSendCop)
                    } else {
                        log.debug { "No available cops or wrong road!" }
                    }
                }
            }
        }
    }

    private fun checkKeyboardInput() = when {
        Gdx.input.isKeyJustPressed(Input.Keys.UP) -> PlayerAction.UP
        Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> PlayerAction.DOWN
        Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> PlayerAction.COP
        else -> PlayerAction.NONE
    }

    private fun checkTouchInput(playerPosition: Vector3) = if (Gdx.input.justTouched()) {
        with (Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())) {
            viewport.unproject(this)
            when {
                x >= playerPosition.x - 1f && y > playerPosition.y -> PlayerAction.UP
                x >= playerPosition.x - 1f && y < playerPosition.y -> PlayerAction.DOWN
                x < playerPosition.x - 2f && round(y) == round(playerPosition.y) -> PlayerAction.COP
                else -> PlayerAction.NONE
            }
        }
    } else {
        PlayerAction.NONE
    }
}
