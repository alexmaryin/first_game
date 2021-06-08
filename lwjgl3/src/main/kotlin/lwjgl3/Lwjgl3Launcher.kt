package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.alexmaryin.firstgame.StartWindow

/** Launches the desktop (LWJGL3) application.  */

fun main() {
    Lwjgl3Application(StartWindow(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("firstGame")
        setWindowedMode(640, 480)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}