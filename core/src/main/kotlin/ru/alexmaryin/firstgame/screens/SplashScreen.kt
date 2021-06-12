package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils.random
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.*

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    private val policeCar = engine.entity {
        with<TransformComponent> { position.set(random(1f, 15f), random(1f, 8f), 0f) }
        with<GraphicComponent>()
        with<PlayerComponent>()
        with<FacingComponent>()
        with<MoveComponent>()
    }

    override fun show() {
        log.debug { "Splash screen showing" }


    }

    override fun render(delta: Float) {

        engine.update(delta)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) -> game.setScreen<MenuScreen>()
//            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> policeCar.transform.position.y += Gameplay.MOVE_STEP
//            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> policeCar.transform.position.y -= Gameplay.MOVE_STEP
//            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> policeCar.transform.position.x -= Gameplay.MOVE_STEP
//            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> policeCar.transform.position.x += Gameplay.MOVE_STEP
        }

//        log.debug { "Render calls ${game.batch.renderCalls}" }
    }
}