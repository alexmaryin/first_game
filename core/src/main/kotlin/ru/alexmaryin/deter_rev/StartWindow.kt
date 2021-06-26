package ru.alexmaryin.deter_rev

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Pool
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
import ktx.preferences.get
import ru.alexmaryin.deter_rev.engine.audio.AudioService
import ru.alexmaryin.deter_rev.engine.audio.DefaultAudioService
import ru.alexmaryin.deter_rev.engine.audio.NoAudioService
import ru.alexmaryin.deter_rev.engine.systems.*
import ru.alexmaryin.deter_rev.screens.GameScreen
import ru.alexmaryin.deter_rev.screens.SplashScreen
import ru.alexmaryin.deter_rev.ui.createSkin
import ru.alexmaryin.deter_rev.values.TextureAtlases
import ru.alexmaryin.deter_rev.values.Textures
import ru.alexmaryin.deter_rev.values.WorldDimens

private val log = logger<StartWindow>()

/** implementation shared by all platforms.  */
class  StartWindow : KtxGame<GameScreen>() {

    val viewport = FitViewport(WorldDimens.WIDTH, WorldDimens.HEIGHT)
    val uiViewport = FitViewport(WorldDimens.ACTUAL_WIDTH, WorldDimens.ACTUAL_HEIGHT)
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
            addSystem(EventSystem(audioService))
            addSystem(PlayerAnimationSystem(graphicsAtlas))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, viewport))
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

    fun pauseEngine() = with(engine) {
        getSystem<SnapMoveSystem>().setProcessing(false)
        getSystem<AnimationSystem>().setProcessing(false)
        getSystem<EnemySystem>().setProcessing(false)
        getSystem<CopSystem>().setProcessing(false)
    }

    fun resumeEngine() = with(engine) {
        getSystem<SnapMoveSystem>().setProcessing(true)
        getSystem<AnimationSystem>().setProcessing(true)
        getSystem<EnemySystem>().setProcessing(true)
        getSystem<CopSystem>().setProcessing(true)
    }

    fun resetEngine() = with(engine) {
        entities.forEach { entity ->
            entity.components.forEach { component ->
                if (component is Pool.Poolable) component.reset()
            }
        }
        removeAllEntities()
        clearPools()
        getSystem<EnemySystem>().reset()
        getSystem<EventSystem>().reset()
        getSystem<SnapMoveSystem>().reset()
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