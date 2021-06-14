package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ktx.collections.GdxArray
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.entities.Cop
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class CopSystem : IteratingSystem(
    allOf(CopComponent::class, TransformComponent::class, MoveComponent::class)
        .exclude(RemoveComponent::class).get()
), EntityListener {

    inner class CopPool : Pool<Cop>() {
        override fun newObject() = Cop(engine)
    }

    private val newPosition = Vector2()
    private val activeCops = GdxArray<Cop>()
    private val copsPool = CopPool()
    val poolSize get() = activeCops.size to copsPool.peak

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val transform = entity.transform
        val graphic = entity.graphic
        val animation = entity.animation

        transform.offset.set(Cop.X_SPRITE_OFFSET, Cop.Y_SPRITE_OFFSET)
        transform.size.set(Cop.WIDTH_SPRITE_RATIO, Cop.HEIGHT_SPRITE_RATIO)
        transform.setInitialPosition(newPosition.x, newPosition.y)
        graphic.flipHorizontal = true
        animation.type = AnimationType.COP_WALK_FROM_LEFT
    }

    override fun entityRemoved(entity: Entity) {
        activeCops.removeValue(entity as Cop, false)
        copsPool.free(entity)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // next move left
        entity.move.moveToPosition(Move.SlowLeft)

        // TODO check collisions and hiding
        if (entity.transform.position.x <= 0f) {
            entity.cop.isMissed = true
            entity.addComponent<RemoveComponent>(engine)
            engine.getSystem<DamageSystem>().addMissedCop()
        }
    }

    fun addCop(position: Vector2) {
        newPosition.set(position)
        val newCop = copsPool.obtain()
        activeCops.add(newCop)
    }
}