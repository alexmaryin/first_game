package ru.alexmaryin.firstgame

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.ashley.getSystem
import ktx.assets.disposeSafely
import ktx.assets.getValue
import ktx.assets.loadOnDemand
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.systems.*
import ru.alexmaryin.firstgame.screens.GameScreen
import ru.alexmaryin.firstgame.screens.GameplayScreen
import ru.alexmaryin.firstgame.screens.MenuScreen
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.WorldDimens

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)
    val uiViewport = FitViewport(
        WorldDimens.F_WIDTH * WorldDimens.F_CELL_SIZE,
        WorldDimens.F_HEIGHT * WorldDimens.F_CELL_SIZE
    )

    private val assetManager = AssetManager()
    private val graphicsAtlas by assetManager.loadOnDemand<TextureAtlas>(GameAssets.GRAPHICS_ATLAS)
    private val environmentAtlas by assetManager.loadOnDemand<TextureAtlas>(GameAssets.ENVIRONMENT_ATLAS)

    private val batch by lazy { SpriteBatch() }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem())
            addSystem(EnemySystem())
            addSystem(CopSystem())
            addSystem(SnapMoveSystem())
            addSystem(CollisionSystem())
            addSystem(EventSystem { pauseEngine() })
            addSystem(PlayerAnimationSystem(graphicsAtlas))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, viewport, uiViewport))
            addSystem(RemoveSystem())
            addSystem(DebugSystem(batch))
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create a game instance" }
        val gameplay = GameplayScreen(this, environmentAtlas)
        addScreen(gameplay)
        addScreen(MenuScreen(this))
        Gdx.input.setCursorPosition(Gdx.graphics.displayMode.width, Gdx.graphics.displayMode.height)
        setScreen<GameplayScreen>()
        gameplay.startGame()
    }

    override fun dispose() {
        log.debug { "Disposed ${batch.maxSpritesInBatch} sprites, ${graphicsAtlas.regions.size + environmentAtlas.regions.size} regions " }
        batch.dispose()
        assetManager.disposeSafely()
    }

    fun pauseEngine() {
        engine.getSystem<SnapMoveSystem>().setProcessing(false)
        engine.getSystem<AnimationSystem>().setProcessing(false)
        engine.getSystem<EnemySystem>().setProcessing(false)
        engine.getSystem<CopSystem>().setProcessing(false)
    }

    fun resumeEngine() {
        engine.getSystem<SnapMoveSystem>().setProcessing(true)
        engine.getSystem<AnimationSystem>().setProcessing(true)
        engine.getSystem<EnemySystem>().setProcessing(true)
        engine.getSystem<CopSystem>().setProcessing(true)
    }
}