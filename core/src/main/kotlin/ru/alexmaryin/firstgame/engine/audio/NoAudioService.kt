package ru.alexmaryin.firstgame.engine.audio

import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets

object NoAudioService : AudioService {
    override fun play(sound: SoundAssets, volume: Float) {}
    override fun play(music: MusicAssets, volume: Float, loop: Boolean) {}
    override fun pause() {}
    override fun resume() {}
    override fun stop(clear: Boolean) {}
    override fun update() {}
}