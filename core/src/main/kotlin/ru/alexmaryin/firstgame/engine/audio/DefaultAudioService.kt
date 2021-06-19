package ru.alexmaryin.firstgame.engine.audio

import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Queue
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.logger
import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets

class DefaultAudioService(
    private val assets: AssetStorage
) : AudioService {

    private val log = logger<DefaultAudioService>()

    inner class PooledSound : Pool.Poolable {
        lateinit var asset: SoundAssets
        var assetVolume = 1f

        override fun reset() {
            assetVolume = 1f
        }
    }

    private val soundsPool = object : Pool<PooledSound>() {
        override fun newObject() = PooledSound()
    }

    private val soundsQueue = Queue<PooledSound>()

    private var currentMusic: MusicAssets? = null

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

    override fun play(music: MusicAssets, volume: Float, loop: Boolean) {
        stop(true)

        KtxAsync.launch {
            if (!assets.isLoaded(music.descriptor))
                assets.loadAsync(music.descriptor).await()
            currentMusic = music
            assets[currentMusic!!.descriptor].apply {
                this.volume = volume
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

    override fun stop(clear: Boolean) {
        currentMusic?.let {
            assets[it.descriptor].stop()
            if (clear) KtxAsync.launch { assets.unload(it.descriptor) }
        }
    }

    override fun update() {
        if (soundsQueue.notEmpty())
            with(soundsQueue.removeFirst()) {
                assets[asset.descriptor].play(assetVolume)
                soundsPool.free(this)
            }
    }
}