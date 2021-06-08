package ru.alexmaryin.firstgame

import com.badlogic.gdx.Game

/** implementation shared by all platforms.  */
class StartWindow : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}