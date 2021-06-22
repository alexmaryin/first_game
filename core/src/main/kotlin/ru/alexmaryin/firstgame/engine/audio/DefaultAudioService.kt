package ru.alexmaryin.firstgame.engine.audio

import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Queue
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets

class DefaultAudioService(
    private val assets: AssetStorage,
    private var musicVolume: Float,
) : AudioService {

    private val log = logger<DefaultAudioService>()

    private var currentMusic: MusicAssets? = null
    private var nextMusic: MusicAssets? = null
    private var fadeIn = false
    private var fadeOut = false
    private var fadeInDelta = 0f
    private var fadeOutDelta = 0f
    private var unloadingMusic: Job? = null

    inner class PooledSound : Pool.Poolable {
        lateinit var asset: SoundAssets
        var assetVolume = 1f
        override fun reset() { assetVolume = 1f }
    }

    private val soundsPool = object : Pool<PooledSound>() {
        override fun newObject() = PooledSound()
    }

    private val soundsQueue = Queue<PooledSound>()

    override fun play(sound: SoundAssets, volume: Float) {
        if (sound.descriptor !in assets) {
            log.debug { "Sound ${sound.descriptor} not loaded!" }
            return
        }

        soundsQueue.addLast(soundsPool.obtain().apply {
            asset = sound
            assetVolume = volume
        })
    }

    override fun play(music: MusicAssets, loop: Boolean) {
        // Set to fade out if any music is playing already
        currentMusic?.let {
            if (assets[it.descriptor].isPlaying)
                fadeOut = true
        }
        // Load music to play and set it to fade in
        KtxAsync.launch {
            if (!assets.isLoaded(music.descriptor)) {
                log.debug { "Loading ${music.descriptor}" }
                assets.loadAsync(music.descriptor).await()
            }
            nextMusic = music
            fadeIn = true
            assets[nextMusic!!.descriptor].apply {
                this.volume = 0f
                isLooping = loop
                play()
            }
        }
    }

    override fun pause() {
        currentMusic?.let {
            assets[it.descriptor].pause()
        }
    }

    override fun resume() {
        currentMusic?.let {
            assets[it.descriptor].play()
        }
    }

    override fun changeVolume(volume: Float) {
        musicVolume = volume
        currentMusic?.let {
            assets[it.descriptor].volume = volume
        }
    }

    override suspend fun stop(clear: Boolean) {
        currentMusic?.let {
            assets[it.descriptor].stop()
            if (clear) {
                log.debug { "Unloading ${currentMusic!!.descriptor}" }
                assets.unload(it.descriptor)
            }
        }
    }

    override fun update(delta: Float) {
        if (fadeOut) {
            fadeOutDelta = delta / Gameplay.CROSS_FADE_DURATION
            with(assets[currentMusic!!.descriptor]) {
                val fadeOutVolume = (volume - fadeOutDelta).coerceAtLeast(0f)
                if (fadeOutVolume == 0f) unloadingMusic = KtxAsync.launch {
                    fadeOut = false
                    fadeOutDelta = 0f
                    stop(true)
                } else volume -= fadeOutDelta
            }
        }
        if (fadeIn) {
            fadeInDelta = delta / Gameplay.CROSS_FADE_DURATION
            with(assets[nextMusic!!.descriptor]) {
                volume = (volume + fadeInDelta).coerceAtMost(musicVolume)
                if (volume == musicVolume) KtxAsync.launch {
                    fadeIn = false
                    fadeInDelta = 0f
                    unloadingMusic?.join()
                    currentMusic = nextMusic
                    nextMusic = null
                }
            }
        }

        // play enqueued sounds
        if (soundsQueue.notEmpty())
            with(soundsQueue.removeFirst()) {
                assets[asset.descriptor].play(assetVolume * musicVolume)
                soundsPool.free(this)
            }
    }
}