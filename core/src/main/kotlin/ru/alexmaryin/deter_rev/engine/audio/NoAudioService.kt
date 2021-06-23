package ru.alexmaryin.deter_rev.engine.audio

import ru.alexmaryin.deter_rev.values.MusicAssets
import ru.alexmaryin.deter_rev.values.SoundAssets

object NoAudioService : AudioService {
    override fun play(sound: SoundAssets, volume: Float) {}
    override fun play(music: MusicAssets, loop: Boolean) {}
    override fun pause() {}
    override fun resume() {}
    override suspend fun stop(clear: Boolean) {}
    override fun update(delta: Float) {}
    override fun changeVolume(volume: Float) {}
}