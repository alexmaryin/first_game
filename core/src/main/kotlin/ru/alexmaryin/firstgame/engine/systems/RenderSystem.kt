package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.GraphicComponent
import ru.alexmaryin.firstgame.engine.components.TransformComponent
import ru.alexmaryin.firstgame.engine.components.graphic
import ru.alexmaryin.firstgame.engine.components.transform

class RenderSystem(
    private val batch: Batch,
    private val viewport: Viewport
) : SortedIteratingSystem(
    allOf(TransformComponent::class, GraphicComponent::class).get(),
    compareBy { entity -> entity[TransformComponent.mapper] }
) {

    private val log = logger<RenderSystem>()

    override fun update(deltaTime: Float) {
        forceSort()
        viewport.apply()
        batch.use(viewport.camera.combined) {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val graphic = entity.graphic

        require(graphic.sprite.texture != null) {
            log.error { "Has no texture to rendering $entity" }
            return
        }

        graphic.sprite.run {
            with(transform.interpolatedPosition) { setBounds(x, y, transform.size.x, transform.size.y) }
            draw(batch)
        }
    }
}