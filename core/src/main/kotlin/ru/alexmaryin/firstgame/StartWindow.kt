package ru.alexmaryin.firstgame

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.engine.systems.PlayerAnimationSystem
import ru.alexmaryin.firstgame.engine.systems.PlayerInputSystem
import ru.alexmaryin.firstgame.engine.systems.RenderSystem
import ru.alexmaryin.firstgame.screens.GameScreen
import ru.alexmaryin.firstgame.screens.MenuScreen
import ru.alexmaryin.firstgame.screens.SplashScreen

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.F_WIDTH, WorldDimens.F_HEIGHT)

    private val defaultRegion by lazy { TextureRegion(Texture(Gdx.files.internal(GameAssets.POLICE_LEFT))) }
    private val upRegion by lazy { TextureRegion(Texture(Gdx.files.internal(GameAssets.POLICE_UP))) }
    private val downRegion by lazy { TextureRegion(Texture(Gdx.files.internal(GameAssets.POLICE_DOWN))) }

    val batch by lazy { SpriteBatch(50) }
    val engine by lazy { PooledEngine().apply {
        addSystem(PlayerInputSystem(viewport))
        addSystem(PlayerAnimationSystem(defaultRegion, upRegion, downRegion))
        addSystem(RenderSystem(batch, viewport)) }
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
        defaultRegion.texture.dispose()
        upRegion.texture.dispose()
        downRegion.texture.dispose()
    }
}