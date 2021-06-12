package ru.alexmaryin.firstgame.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils.random
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.engine.components.FacingComponent
import ru.alexmaryin.firstgame.engine.components.GraphicComponent
import ru.alexmaryin.firstgame.engine.components.PlayerComponent
import ru.alexmaryin.firstgame.engine.components.TransformComponent

private val log = logger<SplashScreen>()

class SplashScreen(game: StartWindow) : GameScreen(game) {

    override fun show() {
        log.debug { "Splash screen showing" }

        engine.entity {
            with<TransformComponent> {
                position.set(random(1f, 15f), random(1f, 8f), 0f)
            }
            with<GraphicComponent> {}
            with<PlayerComponent> {}
            with<FacingComponent> {}

        }
    }

    override fun render(delta: Float) {

        engine.update(delta)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen<MenuScreen>()
        }
    }
}