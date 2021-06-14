package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.entity
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*

class Enemy(engine: Engine) : Entity(), Pool.Poolable {

    init {
        engine.entity {
            with<EnemyComponent>()
            with<TransformComponent>()
            with<AnimationComponent>()
            with<MoveComponent>()
            with<FacingComponent>()
            with<GraphicComponent>()
        }
    }

    override fun reset() {
        components.forEach { component ->
            if (component is Pool.Poolable) component.reset()
        }
    }
}