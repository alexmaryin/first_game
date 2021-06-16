package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.values.Gameplay

class EventSystem : IntervalIteratingSystem(
    allOf(PlayerComponent::class).get(), 1f
), GameEventsListener {

    private var _level = 1
    val level get() = _level

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        EventDispatcher.subscribeOn<GameOver>(this)
        EventDispatcher.subscribeOn<CopCatchEnemy>(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        EventDispatcher.removeSubscriptions(this)
    }

    override fun onEventDelivered(event: GameEvent) {

        when (event) {
            is GameOver -> {
                engine.getSystem<SnapMoveSystem>().setProcessing(false)
                engine.getSystem<AnimationSystem>().setProcessing(false)
                engine.getSystem<EnemySystem>().setProcessing(false)
                engine.getSystem<CopSystem>().setProcessing(false)
                Gdx.input.vibrate(1000)
                engine.getEntitiesFor(allOf(PlayerComponent::class).get()).forEach { player ->
                    player.addComponent<RemoveComponent>(engine) {
                        delay = Gameplay.GAME_OVER_DELAY
                    }
                }
                EventDispatcher.listeners.clear()
            }
            is CopCatchEnemy -> {
                engine.getSystem<SnapMoveSystem>().stopMoving(event.cop, event.enemy)
                with (event.cop) {
                    cop.state = CopState.ATTACK
                    cop.attackTime = Gameplay.nextAttackTime
                    animation.animateCopAttack()
                }
                with (event.enemy) {
                    enemy.state = EnemyState.UNDER_ATTACK
                    enemy.underAttackTime = event.cop.cop.attackTime
                    animation.animateEnemyUnderAttack()
                }
            }
            else -> {}
        }
    }

    override fun processEntity(entity: Entity) {
        _level = entity.player.gameLevel
    }
}