package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.ashley.configureEntity
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*

class PoliceCar(engine: Engine) : Entity() {
    init {
        engine.configureEntity(this) {
            with<TransformComponent> { setInitialPosition(14f,2f, 1f) }
            with<GraphicComponent> { sprite.setSize(1f, 1f) }
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }
    }
}