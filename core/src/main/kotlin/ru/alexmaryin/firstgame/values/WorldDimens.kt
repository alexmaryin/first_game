package ru.alexmaryin.firstgame.values

object WorldDimens {
    const val CELL_SIZE = 80f
    const val WIDTH = 16f
    const val HEIGHT = 9f
    val ROADS_Y_CELLS = listOf(1f, 3f, 5f, 7f)
    private val ENTITIES_LAYERS_Z = listOf(1f, 3f, 5f, 7f)
    val IN_FRONT_LAYERS_Z = listOf(0f, 2f, 4f, 6f)
    const val BACK_LAYER_Z = 8f

    fun getLayerForRoad(road: Float) = ENTITIES_LAYERS_Z[ROADS_Y_CELLS.indexOf(road)]
}