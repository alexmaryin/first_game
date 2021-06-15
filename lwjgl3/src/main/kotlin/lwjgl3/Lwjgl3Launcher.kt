package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.alexmaryin.firstgame.StartWindow
import ru.alexmaryin.firstgame.values.Gameplay
import ru.alexmaryin.firstgame.values.WorldDimens
import kotlin.math.min

/** Launches the desktop (LWJGL3) application.  */

fun main() {
    Lwjgl3Application(StartWindow(), Lwjgl3ApplicationConfiguration().apply {
        setTitle(Gameplay.DEFAULT_TITLE)
        val monitor = Lwjgl3ApplicationConfiguration.getDisplayMode()
        setWindowedMode(
            min(monitor.width, WorldDimens.WIDTH * WorldDimens.CELL_SIZE),
            min(monitor.height, WorldDimens.HEIGHT * WorldDimens.CELL_SIZE)
        )
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}