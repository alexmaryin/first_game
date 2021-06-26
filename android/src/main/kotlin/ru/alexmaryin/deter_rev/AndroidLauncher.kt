package ru.alexmaryin.deter_rev

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import ru.alexmaryin.deter_rev.values.WorldDimens
import kotlin.math.min

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {

    private val metrics = DisplayMetrics()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            useGyroscope = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(metrics)
        } else {
            windowManager.defaultDisplay.getRealMetrics(metrics)
        }
        WorldDimens.ACTUAL_HEIGHT = min(metrics.heightPixels.toFloat(), WorldDimens.HEIGHT * WorldDimens.CELL_SIZE)
        WorldDimens.ACTUAL_WIDTH = min(metrics.widthPixels.toFloat(), WorldDimens.WIDTH * WorldDimens.CELL_SIZE)
        initialize(StartWindow(), config)
    }
}