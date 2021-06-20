package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.*
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.TextureAtlases
import ru.alexmaryin.firstgame.values.WorldDimens.CYRILLIC_CHARS

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlases.UI_ATLAS.descriptor]
    val uiFont = generateFont(GameAssets.RU_FONT)
    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label("default") {
            font = uiFont
            fontColor = Color.WHITE
        }
        label("red") {
            font = uiFont
            fontColor = Color.RED
        }
        checkBox("checkBox") {
            font = uiFont
            fontColor = Color.GRAY
            checkboxOn = skin.getDrawable("BTN_CHECKBOX_IN")
            checkboxOff = skin.getDrawable("BTN_CHECKBOX_OUT")
        }
    }
}

fun generateFont(fontName: String): BitmapFont = FreeTypeFontGenerator(Gdx.files.internal(fontName)).run {
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
        characters = FreeTypeFontGenerator.DEFAULT_CHARS + CYRILLIC_CHARS
        size = 16
        borderColor = Color.BLACK
        borderWidth = 2f
    }
    val font = generateFont(parameter)
    dispose()
    font
}