package ru.alexmaryin.firstgame.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.alexmaryin.firstgame.StartWindow

/** Launches the desktop (LWJGL3) application.  */
object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        createApplication()
    }

    private fun createApplication(): Lwjgl3Application {
        return Lwjgl3Application(StartWindow(), defaultConfiguration)
    }

    private val defaultConfiguration: Lwjgl3ApplicationConfiguration
        get() {
            val configuration = Lwjgl3ApplicationConfiguration()
            configuration.setTitle("firstGame")
            configuration.setWindowedMode(640, 480)
            configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            return configuration
        }
}