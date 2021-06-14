package ru.alexmaryin.firstgame.engine.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.configureEntity
import ktx.ashley.with
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.WorldDimens

class Cop(engine: Engine) : Entity(), Pool.Poolable {

    init {
        engine.configureEntity(this) {
            with<CopComponent>()
            with<TransformComponent>()
            with<AnimationComponent>()
            with<MoveComponent>()
            with<FacingComponent>()
            with<GraphicComponent>()
        }
        engine.addEntity(this)
    }

    override fun reset() {
        components.forEach { component ->
            if (component is Pool.Poolable) component.reset()
        }
    }

    companion object {
        const val WIDTH_SPRITE_RATIO = 0.45f
        const val HEIGHT_SPRITE_RATIO = 0.55f
        const val Y_SPRITE_OFFSET = 15f / WorldDimens.CELL_SIZE
        const val X_SPRITE_OFFSET = 15f / WorldDimens.CELL_SIZE
    }
}