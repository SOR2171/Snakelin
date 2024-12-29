class Tile(val x: Int, val y: Int) {
    var snake = false
    
    fun getPaintX() = x * TILE_WIDTH + TILE_EDGE + BOARD
    
    fun getPaintY() = y * TILE_WIDTH + TILE_EDGE + BOARD + UPPER_BOARD
}