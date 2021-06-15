package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.utils.Pool
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.entities.Enemy
import ru.alexmaryin.firstgame.values.AnimationType
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.Move
import ru.alexmaryin.firstgame.values.WorldDimens

class EnemySystem : IteratingSystem(
    allOf(EnemyComponent::class, TransformComponent::class, MoveComponent::class)
        .exclude(RemoveComponent::class).get()
), EntityListener {

    inner class EnemyPool : Pool<Enemy>() {
        override fun newObject() = Enemy(engine)
    }

    private var _enemiesOnScreen = 0
    val enemiesOnScreen get() = _enemiesOnScreen

    private var _lastEnemyArisen: Float = 0f
    val lastEnemyArisen get() = _lastEnemyArisen

    private val activeEnemies = GdxArray<Enemy>()
    private val enemiesPool = EnemyPool()
    val poolSize get() = activeEnemies.size to enemiesPool.peak
    private val log = logger<EnemySystem>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        _lastEnemyArisen += deltaTime
        if (lastEnemyArisen >= Gameplay.ENEMY_ARISE_MIN_INTERVAL
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
        // next move
        when (enemy.state) {
            EnemyState.WALK_STRAIGHT -> entity.move.moveToPosition(Move.SlowRight)
            EnemyState.UNDER_ATTACK -> {
                enemy.underAttackTime += deltaTime
                if (enemy.underAttackTime >= Gameplay.ENEMY_ATTACK_INTERVAL) {
                    enemy.state = EnemyState.WALK_BACK
                    enemy.underAttackTime = 0f
                }
                return
            }
            EnemyState.WALK_BACK -> entity.move.moveToPosition(Move.SlowLeft)
        }

        // check if enemy reached end of the road or hides
        when {
            entity.transform.position.x >= WorldDimens.F_WIDTH - 1 && enemy.state == EnemyState.WALK_STRAIGHT  -> {
                enemy.finished = true
                removeEnemyFromScreen(entity)
                return
            }
            entity.transform.position.x <= 0  && enemy.state == EnemyState.WALK_BACK -> removeEnemyFromScreen(entity)
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
//        log.debug { "Enemy added to engine $entity" }

        val enemy = entity.enemy
        val animation = entity.animation
        val transform = entity.transform

        _enemiesOnScreen += 1
        transform.offset.set(Enemy.X_SPRITE_OFFSET, Enemy.Y_SPRITE_OFFSET)
        transform.setInitialPosition(0f, enemy.road * 2 + WorldDimens.ROADS_OFFSET_Y, 0f)
        entity.move.speedRatio *= enemy.speedRatio
        animation.type = AnimationType.values()[enemy.enemyVariant]
    }

    override fun entityRemoved(entity: Entity) {
        _enemiesOnScreen -= 1
        Gdx.input.vibrate(100)

        activeEnemies.removeValue(entity as Enemy, false)
        enemiesPool.free(entity)
    }

    private fun addEnemy() {
        val newEnemy = enemiesPool.obtain()
        newEnemy.remove<RemoveComponent>()
        engine.addEntity(newEnemy)
        activeEnemies.add(newEnemy)
        _lastEnemyArisen = 0f
    }

    private fun removeEnemyFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine) { delay = 0.5f }
        if (entity.enemy.finished) engine.getSystem<DamageSystem>().addMissedEnemy()
    }
}