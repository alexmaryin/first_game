package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class Textures(
    file: String,
    dir: String = "graphic",
    val descriptor: AssetDescriptor<Texture> = AssetDescriptor("$dir/$file", Texture::class.java)
) {
    DEBUG_GRID("debug_grid_layout.png")
}

enum class TextureAtlases(
    file: String,
    dir: String = "graphic",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$dir/$file", TextureAtlas::class.java)
) {
    GRAPHIC_ATLAS("sprites.atlas")
}

enum class MusicAssets(
    file: String,
    dir: String = "music",
    val descriptor: AssetDescriptor<Music> = AssetDescriptor("$dir/$file", Music::class.java)
) {
    INTRO_HELP("intro_and_help.mp3"),
    MENU_PAUSE("menu_pause.mp3"),
    GAME_MUSIC_UP_10("gameplay_up_to_10_level.mp3"),
    GAME_MUSIC_FROM_10("gameplay_from_10_level.mp3"),
    GAME_OVER("game_over.mp3")
}

enum class SoundAssets(
    file: String,
    dir: String = "sound",
    val descriptor: AssetDescriptor<Sound> = AssetDescriptor("$dir/$file", Sound::class.java)
) {
    COP_ARISEN("cop_arisen.wav"),
    COP_MISSED("cop_missed.wav"),
    COP_RESTORED("cop_restored.wav"),
    ENEMY_ARISEN("enemy_arisen.wav"),
    ENEMY_MISSED("enemy_missed.wav")
}

object GameAssets {
    const val policeAnim = "police"

    val FRONT_LAYERS = listOf("front_0_layer", "front_2_layer", "front_4_layer", "front_6_layer")
    const val BACK_LAYER = "back_8_layer"
}