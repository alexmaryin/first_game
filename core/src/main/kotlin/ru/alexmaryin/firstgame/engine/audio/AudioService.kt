package ru.alexmaryin.firstgame.engine.audio

import ru.alexmaryin.firstgame.values.MusicAssets
import ru.alexmaryin.firstgame.values.SoundAssets

interface AudioService {
    fun play(sound: SoundAssets, volume: Float = 1f)
    fun play(music: MusicAssets, loop: Boolean = true)
    fun pause()
    fun resume()
    suspend fun stop(clear: Boolean = true)
    fun update(delta: Float)
}