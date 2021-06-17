package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.ashley.configureEntity
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.WorldDimens

class Enemy(engine: Engine) : Entity() {

    init {
        engine.configureEntity(this) {
            with<EnemyComponent>()
            with<TransformComponent> {
                size.set(WIDTH_SPRITE_RATIO, HEIGHT_SPRITE_RATIO)
                offset.set(X_SPRITE_OFFSET, Y_SPRITE_OFFSET)
            }
            with<AnimationComponent>()
            with<MoveComponent>()
            with<FacingComponent>()
            with<GraphicComponent>()
        }
    }

    companion object {
        private const val WIDTH_SPRITE_RATIO = 2f
        private const val HEIGHT_SPRITE_RATIO = 2f
        private const val Y_SPRITE_OFFSET = -30f / WorldDimens.F_CELL_SIZE
        private const val X_SPRITE_OFFSET = 0f / WorldDimens.F_CELL_SIZE
    }
}