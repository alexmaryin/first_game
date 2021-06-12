package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.ashley.allOf
import ru.alexmaryin.firstgame.engine.components.*

class PlayerAnimationSystem(
    private val defaultRegion: TextureRegion,
    private val upRegion: TextureRegion,
    private val downRegion: TextureRegion
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
            graphic.setSpriteRegion(when(direction) {
                FacingDirection.UP -> upRegion
                FacingDirection.DOWN -> downRegion
                else -> defaultRegion
            })
        }
    }

    override fun entityAdded(entity: Entity) {
        entity.graphic.setSpriteRegion(defaultRegion)
    }

    override fun entityRemoved(entity: Entity) {}
}