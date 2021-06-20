package ru.alexmaryin.firstgame

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.ashley.getSystem
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import ru.alexmaryin.firstgame.engine.audio.AudioService
import ru.alexmaryin.firstgame.engine.audio.DefaultAudioService
import ru.alexmaryin.firstgame.engine.audio.NoAudioService
import ru.alexmaryin.firstgame.engine.systems.*
import ru.alexmaryin.firstgame.screens.GameScreen
import ru.alexmaryin.firstgame.screens.SplashScreen
import ru.alexmaryin.firstgame.ui.createSkin
import ru.alexmaryin.firstgame.values.TextureAtlases
import ru.alexmaryin.firstgame.values.Textures
import ru.alexmaryin.firstgame.values.WorldDimens

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class  StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.WIDTH, WorldDimens.HEIGHT)
    val uiViewport = FitViewport(
        WorldDimens.WIDTH * WorldDimens.CELL_SIZE,
        WorldDimens.HEIGHT * WorldDimens.CELL_SIZE
    )
    val stage: Stage by lazy { Stage(uiViewport, batch).apply { Gdx.input.inputProcessor = this } }

    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }

    private val graphicsAtlas by lazy { assets[TextureAtlases.GRAPHIC_ATLAS.descriptor] }
    val preferences: Preferences by lazy { Gdx.app.getPreferences("deter_revolution_0_1") }
    private val defaultAudioService by lazy { DefaultAudioService(assets, preferences["music_volume", 0.5f]) }
    lateinit var audioService: AudioService

    private val batch by lazy { SpriteBatch() }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(viewport))
            addSystem(EnemySystem(audioService))
            addSystem(CopSystem(audioService))
            addSystem(SnapMoveSystem())
            addSystem(CollisionSystem())
            addSystem(EventSystem(audioService) { event ->
                pauseEngine()
                preferences.flush { this["last_scores"] = event.score }
            })
            addSystem(PlayerAnimationSystem(graphicsAtlas))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, viewport, uiViewport))
            addSystem(RemoveSystem())
            addSystem(DebugSystem(batch, assets[Textures.DEBUG_GRID.descriptor]))
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        Gdx.input.setCursorPosition(Gdx.graphics.displayMode.width, Gdx.graphics.displayMode.height)
        log.debug { "Create a game instance" }
        setAudio(preferences["audio_on", true])

        val assetRefs = gdxArrayOf(
            TextureAtlases.values().filter { it.isSkinAtlas }.map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            createSkin(assets)
            addScreen(SplashScreen(this@StartWindow))
            setScreen<SplashScreen>()
        }
    }

    override fun dispose() {
        log.debug { "Disposed ${batch.maxSpritesInBatch} sprites, ${graphicsAtlas.regions.size} regions" }
        batch.disposeSafely()
        assets.disposeSafely()
        stage.disposeSafely()
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

    fun setAudio(on: Boolean) {
        if (on) {
            audioService = defaultAudioService
            audioService.resume()
        } else {
            defaultAudioService.pause()
            audioService = NoAudioService
        }
    }
}