package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.random
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.EnemyMissed
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.values.*

class EnemySystem : IteratingSystem(
    allOf(EnemyComponent::class).exclude(RemoveComponent::class).get()
) {

    private var _enemiesOnScreen = 0
    private val enemiesOnScreen get() = _enemiesOnScreen

    private var _lastEnemyArisen: Float = 0f
    private val lastEnemyArisen get() = _lastEnemyArisen

    private val log = logger<EnemySystem>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        _lastEnemyArisen += deltaTime
        if (lastEnemyArisen >= Gameplay.nextEnemyInterval
            && enemiesOnScreen < Gameplay.MAX_AVAILABLE_COPS * Gameplay.DIFFICULTY_RATIO
        ) {
            if (random(1, 100) % 2 == 0) {
                addEnemy()
            } else {
                _lastEnemyArisen = 0f
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val enemy = entity.enemy
        val level = engine.getSystem<EventSystem>().level

        // next move
        when (enemy.state) {
            EnemyState.WALK_STRAIGHT -> entity.move.moveToPosition(MoveRight(level))
            EnemyState.WALK_BACK -> entity.move.moveToPosition(MoveLeft(level))
            EnemyState.UNDER_ATTACK -> {
                entity.facing?.direction = FacingDirection.RIGHT
                enemy.underAttackTime -= deltaTime
                if (enemy.underAttackTime <= 0) {
                    enemy.state = EnemyState.WALK_BACK
                    entity.animation.setPreviousAnimation()
                }
                return
            }
        }

        // check if enemy reached end of the road or hides
        when {
            entity.transform.position.x >= WorldDimens.F_WIDTH - 1f && enemy.state == EnemyState.WALK_STRAIGHT  -> {
                EventDispatcher.send(EnemyMissed)
                removeEnemyFromScreen(entity)
                return
            }
            entity.transform.position.x <= 0f  && enemy.state == EnemyState.WALK_BACK -> removeEnemyFromScreen(entity)
        }
    }

    private fun addEnemy() {
        engine.entity {
            val enemy = with<EnemyComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
            with<AnimationComponent> { type = AnimationType.values()[enemy.enemyVariant] }
            with<TransformComponent> {
                size.set(Entities.ENEMY_WIDTH_SPRITE_RATIO, Entities.ENEMY_HEIGHT_SPRITE_RATIO)
                offset.set(Entities.ENEMY_X_SPRITE_OFFSET, Entities.ENEMY_Y_SPRITE_OFFSET)
                setInitialPosition(0f, enemy.road, 1f)
                log.debug { "Add enemy at position $position" }
            }
            with<GraphicComponent>()
        }
        _enemiesOnScreen += 1
        _lastEnemyArisen = 0f
    }

    private fun removeEnemyFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
        _enemiesOnScreen -= 1
        Gdx.input.vibrate(100)
    }
}