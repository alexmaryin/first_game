package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ru.alexmaryin.firstgame.engine.audio.AudioService
import ru.alexmaryin.firstgame.engine.audio.DefaultAudioService
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.*
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets
import kotlin.math.ceil

class EventSystem(
    private val audioService: AudioService,
    private val gameOverCallback: () -> Unit
) : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), 1f
), GameEventsListener {

    private var _level = 1
    val level get() = _level

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        EventDispatcher.subscribeOn<GameOver>(this)
        EventDispatcher.subscribeOn<CopCatchEnemy>(this)
        EventDispatcher.subscribeOn<PlayerRestoresCop>(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        EventDispatcher.removeSubscriptions(this)
    }

    override fun onEventDelivered(event: GameEvent) {

        when (event) {
            is GameOver -> {
                gameOverCallback()
                (engine as PooledEngine).clearPools()
                audioService.play(MusicAssets.GAME_OVER)
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
                val attackTime = Gameplay.nextAttackTime
                with (event.cop) {
                    cop.state = CopState.ATTACK
                    cop.attackTime = attackTime
                    animation.animateCopAttack()
                }
                with (event.enemy) {
                    enemy.state = EnemyState.UNDER_ATTACK
                    enemy.underAttackTime = attackTime
                    animation.animateEnemyUnderAttack()
                }
            }

            is PlayerRestoresCop -> {
                engine.getSystem<CopSystem>().removeCopFromScreen(event.cop)
                audioService.play(SoundAssets.COP_RESTORED)
            }

            else -> {}
        }
    }

    override fun processEntity(entity: Entity) {
        val checkLevel = MathUtils.clamp(ceil(entity.player.enemiesCaught / Gameplay.LEVEL_UP).toInt(), 1, Gameplay.LEVELS_MAX)
        if (checkLevel > _level) EventDispatcher.send(LevelUp(checkLevel))
        _level = checkLevel
    }
}