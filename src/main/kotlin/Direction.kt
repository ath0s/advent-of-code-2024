enum class Direction(private val coordinate: Coordinate) {
    NORTH_WEST(Coordinate(-1,-1)),
    NORTH(Coordinate(0,-1)),
    NORTH_EAST(Coordinate(1,-1)),
    EAST(Coordinate(1,0)),
    SOUTH_EAST(Coordinate(1,1)),
    SOUTH(Coordinate(0,1)),
    SOUTH_WEST(Coordinate(-1,1)),
    WEST(Coordinate(-1,0));

    fun move(coordinate: Coordinate) =
        coordinate + this.coordinate
}

infix fun Coordinate.move(direction: Direction) =
    direction.move(this)
