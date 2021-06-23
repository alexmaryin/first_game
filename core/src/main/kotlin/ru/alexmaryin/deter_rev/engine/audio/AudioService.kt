package ru.alexmaryin.deter_rev.engine.audio

import ru.alexmaryin.deter_rev.values.MusicAssets
import ru.alexmaryin.deter_rev.values.SoundAssets

interface AudioService {
    fun play(sound: SoundAssets, volume: Float = 1f)
    fun play(music: MusicAssets, loop: Boolean = true)
    fun pause()
    fun resume()
    suspend fun stop(clear: Boolean = true)
    fun update(delta: Float)
    fun changeVolume(volume: Float)
}