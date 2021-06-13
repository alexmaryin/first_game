package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.ashley.allOf
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.RotationDeg

class PlayerAnimationSystem(
    private val atlas: TextureAtlas
    ) : IteratingSystem(allOf(
    PlayerComponent::class,
    FacingComponent::class,
    GraphicComponent::class
).get()), EntityListener {

    private var lastDirection = FacingDirection.DEFAULT

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity.facing
        val graphic = entity.graphic

        if(facing.direction == lastDirection && graphic.sprite.texture != null) {
            return
        }

        lastDirection = facing.direction.also { direction ->
            graphic.setSpriteRegion (atlas.findRegion(GameAssets.policeAnim), when (direction) {
                FacingDirection.UP -> RotationDeg.UP
                FacingDirection.DOWN -> RotationDeg.DOWN
                FacingDirection.RIGHT -> RotationDeg.RIGHT
                FacingDirection.LEFT -> RotationDeg.LEFT
                else -> RotationDeg.LEFT
            })
        }
    }

    override fun entityAdded(entity: Entity) {
        entity.graphic.setSpriteRegion(atlas.findRegion(GameAssets.policeAnim), 270f)
    }

    override fun entityRemoved(entity: Entity) {}
}