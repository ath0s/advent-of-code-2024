import java.nio.file.Path
import kotlin.io.path.readLines

class Day18 : Day {
    override fun partOne(filename: String, verbose: Boolean) =
        partOne(filename, Coordinate(70, 70), 1024)

    fun partOne(filename: String, bottomRight: Coordinate, numberOfBytes: Int): Int {
        val bytes = filename.asPath().parseBytes()
        return bytes.take(numberOfBytes).traverse(bottomRight)!!
    }

    override fun partTwo(filename: String, verbose: Boolean) =
        partTwo(filename, Coordinate(70, 70))

    fun partTwo(filename: String, bottomRight: Coordinate): String {
        val bytes = filename.asPath().parseBytes()
        return bytes.withIndex()
            .first { (index) ->
                bytes.take(index + 1).traverse(bottomRight) == null
            }
            .let { (_, coordinate) ->
                coordinate.toString()
            }
    }

    private fun Path.parseBytes() =
        readLines()
            .map {
                val (x, y) = it.split(",")
                Coordinate(x.toInt(), y.toInt())
            }

    private fun List<Coordinate>.traverse(bottomRight: Coordinate): Int? {
        val corrupted = toSet()
        val queue = ArrayDeque(listOf(Coordinate(0, 0) to 0))
        val seen = mutableSetOf<Coordinate>()

        while (queue.isNotEmpty()) {
            val (place, cost) = queue.removeFirst()

            if (place == bottomRight) return cost
            else if (seen.add(place)) {
                queue.addAll(Direction.CARDINAL.map { place + it }
                    .filter { it.inRange(bottomRight) }
                    .filterNot { it in corrupted }
                    .map { it to cost + 1 })
            }
        }
        return null
    }

    private fun Coordinate.inRange(end: Coordinate): Boolean =
        x in 0..end.x && y in 0..end.y

    companion object : Day.Main("Day18.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}