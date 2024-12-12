import Direction.EAST
import Direction.NORTH
import Direction.NORTH_EAST
import Direction.NORTH_WEST
import Direction.SOUTH
import Direction.SOUTH_EAST
import Direction.SOUTH_WEST
import Direction.WEST

enum class Direction(private val coordinate: Coordinate) {
    NORTH_WEST(Coordinate(-1, -1)),
    NORTH(Coordinate(0, -1)),
    NORTH_EAST(Coordinate(1, -1)),
    EAST(Coordinate(1, 0)),
    SOUTH_EAST(Coordinate(1, 1)),
    SOUTH(Coordinate(0, 1)),
    SOUTH_WEST(Coordinate(-1, 1)),
    WEST(Coordinate(-1, 0));

    fun move(coordinate: Coordinate) =
        coordinate + this.coordinate

    companion object {
        val CARDINAL = listOf(NORTH, EAST, SOUTH, WEST)
    }
}

infix fun Coordinate.move(direction: Direction) =
    direction.move(this)

fun Direction.turn90DegreesRight() =
    when (this) {
        NORTH_WEST -> NORTH_EAST
        NORTH -> EAST
        NORTH_EAST -> SOUTH_EAST
        EAST -> SOUTH
        SOUTH_EAST -> SOUTH_WEST
        SOUTH -> WEST
        SOUTH_WEST -> NORTH_WEST
        WEST -> NORTH
    }

fun Direction.inverse() =
    when (this) {
        NORTH_WEST -> SOUTH_EAST
        NORTH -> SOUTH
        NORTH_EAST -> SOUTH_WEST
        EAST -> WEST
        SOUTH_EAST -> NORTH_WEST
        SOUTH -> NORTH
        SOUTH_WEST -> NORTH_EAST
        WEST -> EAST
    }