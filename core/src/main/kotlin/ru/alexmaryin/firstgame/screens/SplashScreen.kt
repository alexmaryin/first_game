package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Gameplay
import kotlin.math.min

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val policeCar = engine.entity {
        with<TransformComponent> { setInitialPosition(14f,2f) }
        with<GraphicComponent> { sprite.setSize(1f, 1f) }
        with<PlayerComponent>()
        with<FacingComponent>()
        with<MoveComponent>()
    }

    override fun show() {
        log.debug { "Splash screen showing" }
    }

    override fun render(delta: Float) {

        engine.update(min(delta, Gameplay.MIN_DELTA_TME))

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) -> game.setScreen<MenuScreen>()
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> {
                engine.entity {
                    with<TransformComponent> { setInitialPosition(0f, policeCar.transform.position.y) }
                    with<AnimationComponent> { type = AnimationType.WALK_FROM_LEFT.apply{ speedRate = 0.4f }  }
                    with<MoveComponent>()
                    with<FacingComponent>()
                    with<GraphicComponent>()
                }
            }
        }

//        log.debug { "Render calls ${game.batch.renderCalls}" }
    }
}