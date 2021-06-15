package ru.alexmaryin.firstgame

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.systems.*
import ru.alexmaryin.firstgame.screens.GameScreen
import ru.alexmaryin.firstgame.screens.MenuScreen
import ru.alexmaryin.firstgame.screens.SplashScreen
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.WorldDimens

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal(GameAssets.GRAPHICS_ATLAS)) }

    val batch by lazy { SpriteBatch(100) }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(DebugSystem(batch))
            addSystem(PlayerInputSystem())
            addSystem(EnemySystem())
            addSystem(CopSystem())
            addSystem(SnapMoveSystem())
            addSystem(DamageSystem())
            addSystem(PlayerAnimationSystem(graphicsAtlas))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, viewport))
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create a game instance" }
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        Gdx.input.setCursorPosition(Gdx.graphics.displayMode.width, Gdx.graphics.displayMode.height)
        setScreen<SplashScreen>()
    }

    override fun dispose() {
        log.debug { "Disposed ${batch.maxSpritesInBatch} sprites" }
        batch.dispose()
        graphicsAtlas.dispose()
    }
}