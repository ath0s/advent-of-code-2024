import kotlin.math.abs

data class LongCoordinate(
    val x: Long,
    val y: Long
) {
    override fun toString() = "$x,$y"

    infix fun manhattanDistance(other: LongCoordinate): Long =
        abs(x - other.x) + abs(y - other.y)
}

operator fun LongCoordinate.plus(other: LongCoordinate) =
    LongCoordinate(x + other.x, y + other.y)
