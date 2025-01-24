object Settings {
    var x = MAP_X_DEFAULT
    var y = MAP_Y_DEFAULT
    var score = 0
    var difficulty = Difficulty.N
    var debug = true
    var isPause = debug

    fun widgetWidth() = x * TILE_WIDTH + BOARD * 2
    fun widgetHeight() = y * TILE_WIDTH + BOARD * 2 + UPPER_BOARD
}