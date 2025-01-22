class Snake(
        var position: Tile,
        var toward: Snake? = null
    ) {
    var dir = Direction.U
    
    fun goAhead(map: MapType) {
        position = toward?.position ?: when (dir) {
            Direction.U -> map[position.x][position.y - 1]
            Direction.D -> map[position.x][position.y + 1]
            Direction.R -> map[position.x + 1][position.y]
            Direction.L -> map[position.x - 1][position.y]
        }
    }
    
    fun addBody(): Snake = Snake(this.position, this)
}