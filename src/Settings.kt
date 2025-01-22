object Settings {
    var x = 15
    var y = 15
    var score = 0
    var isPause = false
    var difficulty = Difficulty.M
    var debug = true

    fun widgetWidth() = x * TILE_WIDTH + BOARD * 2
    fun widgetHeight() = y * TILE_WIDTH + BOARD * 2 + UPPER_BOARD
}