class Snake(
    var position: Tile,
    var toward: Snake? = null
) {
    var dir = Direction.U

    fun goAhead(map: MapType, gf: GameFrame) {
        position = toward?.position ?: when (dir) {
            Direction.U -> {
                if (position.y - 1 < 0 || !map[position.x][position.y - 1].snake) {
                    gf.gameLose()
                    return
                } else map[position.x][position.y - 1]
            }

            Direction.D -> {
                if (position.y + 1 >= Settings.y || !map[position.x][position.y + 1].snake) {
                    gf.gameLose()
                    return
                } else map[position.x][position.y + 1]
            }

            Direction.R -> {
                if (position.x + 1 >= Settings.x || !map[position.x + 1][position.y].snake) {
                    gf.gameLose()
                    return
                } else map[position.x + 1][position.y]
            }

            Direction.L -> {
                if (position.x - 1 < 0 || !map[position.x - 1][position.y].snake) {
                    gf.gameLose()
                    return
                } else map[position.x - 1][position.y]
            }
        }
    }

    fun addBody(): Snake = Snake(this.position, this)
}