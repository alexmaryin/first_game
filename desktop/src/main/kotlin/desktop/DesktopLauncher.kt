package desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import ru.alexmaryin.deter_rev.StartWindow
import ru.alexmaryin.deter_rev.values.Gameplay
import ru.alexmaryin.deter_rev.values.WorldDimens
import kotlin.math.min

fun main() {
    val monitor = Lwjgl3ApplicationConfiguration.getDisplayMode()
    WorldDimens.ACTUAL_WIDTH = min(monitor.width.toFloat(), WorldDimens.WIDTH * WorldDimens.CELL_SIZE)
    WorldDimens.ACTUAL_HEIGHT = min(monitor.height.toFloat(), WorldDimens.HEIGHT * WorldDimens.CELL_SIZE)
    Lwjgl3Application(StartWindow(), Lwjgl3ApplicationConfiguration().apply {
        setTitle(Gameplay.DEFAULT_TITLE)
        setWindowedMode(WorldDimens.ACTUAL_WIDTH.toInt(), WorldDimens.ACTUAL_HEIGHT.toInt() )
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}