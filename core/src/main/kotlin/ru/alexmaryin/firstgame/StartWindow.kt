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

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal(GameAssets.GRAPHICS_ATLAS)) }

    val batch by lazy { SpriteBatch(50) }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(viewport))
            addSystem(MoveSystem())
            addSystem(DamageSystem())
            addSystem(
                PlayerAnimationSystem(
                    graphicsAtlas.findRegion("police_up"),
                    graphicsAtlas.findRegion("police_down"),
                    graphicsAtlas.findRegion("police_left"),
                    graphicsAtlas.findRegion("police_right"),
                )
            )
            addSystem(RenderSystem(batch, viewport))
            addSystem(RemoveSystem())
            addSystem(DebugSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        log.debug { "Create a game instance" }
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        setScreen<SplashScreen>()
    }

    override fun dispose() {
        log.debug { "Disposed ${batch.maxSpritesInBatch} sprites" }
        batch.dispose()
        graphicsAtlas.dispose()
    }
}