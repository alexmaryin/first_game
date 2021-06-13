package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.GdxRuntimeException
import ktx.ashley.*
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class EnemySystem : IteratingSystem(
    allOf(EnemyComponent::class, TransformComponent::class, MoveComponent::class)
        .exclude(PlayerComponent::class, RemoveComponent::class).get()), EntityListener {

    private var enemiesOnScreen = 0

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        // next move right
        entity.move.moveToPosition(Move.SlowRight)

        // check if enemy reached end of the road
        if (entity.transform.position.x >= WorldDimens.F_WIDTH - 1) {
            entity.addComponent<RemoveComponent>(engine) { delay = 0.5f }
            engine.getSystem<DamageSystem>().addMissedEnemy()
        }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val enemy = entity.enemy
        val animation = entity.animation
        val transform = entity.transform
        require(animation != null)

        enemiesOnScreen += 1
        transform.setInitialPosition(0f, enemy.road * 2 + WorldDimens.ROADS_OFFSET_Y, 0f)
        entity.move.speedRatio *= enemy.speedRatio
        animation.type = when(enemy.enemyVariant) {
            1 -> AnimationType.ENEMY1_WALK_FROM_LEFT
            2 -> AnimationType.ENEMY2_WALK_FROM_LEFT
            else -> throw GdxRuntimeException("Animation for enemy ${enemy.enemyVariant} is not implemented yet")
        }.apply { speedRate = 0.4f }
    }

    override fun entityRemoved(entity: Entity) { enemiesOnScreen -= 1 }

    fun addEnemy() {
        engine.entity {
            with<EnemyComponent>()
            with<TransformComponent>()
            with<AnimationComponent>()
            with<MoveComponent>()
            with<FacingComponent>()
            with<GraphicComponent>()
        }
    }
}