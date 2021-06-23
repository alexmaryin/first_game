package ru.alexmaryin.deter_rev.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.GdxRuntimeException
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.error
import ktx.log.logger
import ru.alexmaryin.deter_rev.engine.components.*
import ru.alexmaryin.deter_rev.values.AnimationType
import java.util.*

class AnimationSystem(
    private val atlas: TextureAtlas
) : IteratingSystem(
    allOf(AnimationComponent::class, GraphicComponent::class)
        .exclude(RemoveComponent::class).get()
), EntityListener {

    private val animationCache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)
    private val log = logger<AnimationSystem>()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    private fun loadAnimation(type: AnimationType): Animation2D = animationCache[type] ?: run {
        val regions = atlas.findRegions(type.atlasKey)
        if (regions.isEmpty) throw GdxRuntimeException("Animation for ${type.atlasKey} has not found")
        val animation = Animation2D(type, regions)
        animationCache[type] = animation
        animation
    }

    override fun entityAdded(entity: Entity) {
        entity.animation.run {
            animation = loadAnimation(type)
            val frame = animation.getKeyFrame(stateTime)
            entity.graphic.setSpriteRegion(frame)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animation = entity.animation
        require(animation.type != AnimationType.NONE) {
            log.error { "Animation for component $animation $entity has no defined type or animation" }
            return
        }
        val graphic = entity.graphic

        if (animation.type == animation.animation.type) {
            animation.stateTime += deltaTime
        } else {
            animation.stateTime = 0f
            animation.animation = loadAnimation(animation.type)
        }

        val frame = animation.animation.getKeyFrame(animation.stateTime)
        graphic.setSpriteRegion(frame)
    }

    override fun entityRemoved(entity: Entity) {}
}