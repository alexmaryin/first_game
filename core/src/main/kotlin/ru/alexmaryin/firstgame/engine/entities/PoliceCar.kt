package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.ashley.configureEntity
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.WorldDimens

class PoliceCar(engine: Engine) : Entity() {

    init {
        engine.configureEntity(this) {
            with<PlayerComponent>()
            with<TransformComponent> {
                size.set(WIDTH_SPRITE_RATIO, HEIGHT_SPRITE_RATIO)
//                offset.set(0f, -50f)
                setInitialPosition(14f,2f, 1f)
            }
            with<GraphicComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }
    }

    companion object {
        private const val WIDTH_SPRITE_RATIO = 1.5f
        private const val HEIGHT_SPRITE_RATIO = 1.5f
        private const val Y_SPRITE_OFFSET = 0f / WorldDimens.F_CELL_SIZE
        private const val X_SPRITE_OFFSET = 0f / WorldDimens.F_CELL_SIZE
    }
}