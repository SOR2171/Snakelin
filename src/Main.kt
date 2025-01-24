typealias MapType = Array<Array<Tile>>

const val MAP_X_DEFAULT = 10
const val MAP_Y_DEFAULT = 10
const val MAP_X_MAX = 20
const val MAP_Y_MAX = 20
const val MAP_X_MIN = 5
const val MAP_Y_MIN = 5

const val TILE_WIDTH = 30
const val TILE_EDGE = 2
const val BOARD = 20
const val UPPER_BOARD = 50
const val GAME_NAME = "Snakelin"

const val SW_WIDTH = 400
const val SW_HEIGHT = 300

fun main() {
    val gameFrame = GameFrame()

    while (true) {
        if (!Settings.isPause) gameFrame.goNextTurn()
        Thread.sleep(gameFrame.roundTime)
    }
}