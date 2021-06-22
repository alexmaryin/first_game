package ru.alexmaryin.firstgame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.*
import ru.alexmaryin.firstgame.values.GameAssets
import ru.alexmaryin.firstgame.values.TextureAtlases
import ru.alexmaryin.firstgame.values.WorldDimens.CYRILLIC_CHARS

fun createSkin(assets: AssetStorage) {
    val atlas = assets[TextureAtlases.UI_ATLAS.descriptor]
    val uiFontBordered = generateFont(GameAssets.RU_FONT, true)
    val uiFont = generateFont(GameAssets.RU_FONT, false)
    Scene2DSkin.defaultSkin = skin(atlas) { skin ->
        label("default") {
            font = uiFont
            fontColor = Color.WHITE
        }
        label("black") {
            font = uiFont
            fontColor = Color.BLACK
        }
        label("default_bordered") {
            font = uiFontBordered
            fontColor = Color.WHITE
        }
        label("red") {
            font = uiFontBordered
            fontColor = Color.RED
        }
        label("secondary") {
            font = uiFontBordered
            fontColor = Color.WHITE.apply { a = 0.7f }
        }
        checkBox("checkBox") {
            font = uiFont
            fontColor = Color.GRAY
            checkboxOn = skin.getDrawable("BTN_CHECKBOX_IN")
            checkboxOff = skin.getDrawable("BTN_CHECKBOX_OUT")
        }
        slider("default-horizontal") {
            background = skin.getDrawable("UI_FULLBAR")
            knob = skin.getDrawable("BTN_SLIDER_SM (9)")
            knobAfter = skin.getDrawable("UI_BARFRAME")
            knobBefore = skin.getDrawable("UI_COLORBAR (1)")
        }
        window("dialog") {
            titleFont = uiFont
            titleFontColor = Color.YELLOW
            background = skin.getDrawable("dilaoguebox")
            stageBackground = skin.getDrawable("black_transparent_back")
        }
        textButton("yellow_button") {
            font = uiFontBordered
            fontColor = Color.YELLOW
            up = skin.getDrawable("BTN_ORANGE_RECT_OUT")
            down = skin.getDrawable("BTN_ORANGE_RECT_IN")
        }
        textButton("green_button") {
            font = uiFontBordered
            fontColor = Color.YELLOW
            up = skin.getDrawable("BTN_GREEN_RECT_OUT")
            down = skin.getDrawable("BTN_GREEN_RECT_IN")
        }
        scrollPane("default_scroll") {
            vScroll = skin.getDrawable("h_scroll")
            vScrollKnob = skin.getDrawable("BTN_RADIO_IN")
        }
    }
}

fun generateFont(fontName: String, withBorder: Boolean = false): BitmapFont = FreeTypeFontGenerator(Gdx.files.internal(fontName)).run {
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
        characters = FreeTypeFontGenerator.DEFAULT_CHARS + CYRILLIC_CHARS
        size = 16
        if (withBorder){
            borderColor = Color.BLACK
            borderWidth = 2f
        }
    }
    val font = generateFont(parameter)
    dispose()
    font
}