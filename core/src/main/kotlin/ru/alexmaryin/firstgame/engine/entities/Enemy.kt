package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.configureEntity
import ktx.ashley.remove
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.WorldDimens

class Enemy(engine: Engine) : Entity(), Pool.Poolable {

    init {
        engine.configureEntity(this) {
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

    companion object {
        const val Y_SPRITE_OFFSET = -5f / WorldDimens.CELL_SIZE
        const val X_SPRITE_OFFSET = 0f / WorldDimens.CELL_SIZE
    }
}