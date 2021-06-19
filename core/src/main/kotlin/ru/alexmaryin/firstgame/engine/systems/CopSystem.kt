package ru.alexmaryin.firstgame.engine.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.*
import ru.alexmaryin.firstgame.engine.audio.AudioService
import ru.alexmaryin.firstgame.engine.components.*
import ru.alexmaryin.firstgame.engine.events.CopMissed
import ru.alexmaryin.firstgame.engine.events.EnemyCaught
import ru.alexmaryin.firstgame.engine.events.EventDispatcher
import ru.alexmaryin.firstgame.values.*

class CopSystem(private val audioService: AudioService) : IteratingSystem(
    allOf(CopComponent::class).exclude(RemoveComponent::class).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform
        val cop = entity.cop
        val level = engine.getSystem<EventSystem>().level

        when (cop.state) {
            CopState.WALK_TO_ENEMY -> entity.move.moveToPosition(MoveLeft(level))
            CopState.WALK_BACK -> entity.move.moveToPosition(MoveRight(level))
            CopState.ATTACK -> {
                cop.attackTime -= deltaTime
                entity.facing?.direction = FacingDirection.LEFT
                if (cop.attackTime <= 0) {
                    cop.state = CopState.WALK_BACK
                    entity.animation.setPreviousAnimation()
                    EventDispatcher.send(EnemyCaught)
                }
                return          // No need check other states if cop is attacking
            }
        }

        if (transform.position.x <= 0f || transform.position.x >= WorldDimens.WIDTH - 1f) {
            EventDispatcher.send(CopMissed)
            removeCopFromScreen(entity)
            audioService.play(SoundAssets.COP_MISSED)
        }
    }

    fun addCop(road: Float) {
        engine.entity {
            with<CopComponent>()
            with<MoveComponent> { initialSpeed = 0.05f }
            with<FacingComponent>()
            with<TransformComponent> {
                size.set(Entities.COP_WIDTH_SPRITE_RATIO, Entities.COP_HEIGHT_SPRITE_RATIO)
                offset.set(Entities.COP_X_SPRITE_OFFSET, Entities.COP_Y_SPRITE_OFFSET)
                setInitialPosition(14f, road, WorldDimens.getLayerForRoad(road))
            }
            with<AnimationComponent> { type = AnimationType.COP_WALK_FROM_LEFT }
            with<GraphicComponent>()
        }
        audioService.play(SoundAssets.COP_ARISEN)
    }

    fun removeCopFromScreen(entity: Entity) {
        entity.addComponent<RemoveComponent>(engine)
        Gdx.input.vibrate(100)
    }
}