package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils.random
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.Gameplay
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.*
import kotlin.math.min

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val policeCar = engine.entity {
        with<TransformComponent> { setInitialPosition(random(1f, 15f), random(1f, 8f)) }
        with<GraphicComponent>()
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
        }

//        log.debug { "Render calls ${game.batch.renderCalls}" }
    }
}