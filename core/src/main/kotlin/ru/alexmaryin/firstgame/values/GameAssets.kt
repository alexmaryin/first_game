package ru.alexmaryin.firstgame.values

import com.badlogic.gdx.assets.AssetDescriptor
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

object GameAssets {
    const val policeAnim = "police"

    val FRONT_LAYERS = listOf("front_0_layer", "front_2_layer", "front_4_layer", "front_6_layer")
    const val BACK_LAYER = "back_8_layer"
}