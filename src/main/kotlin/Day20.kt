import java.nio.file.Path
import kotlin.io.path.readText

class Day20 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int =
        partOne(filename, verbose, 100)

    fun partOne(filename: String, verbose: Boolean, goal: Int): Int =
        filename.asPath().parseTrack(verbose).findCheats(goal, 2)

    override fun partTwo(filename: String, verbose: Boolean): Int =
        partTwo(filename, verbose, 100)

    fun partTwo(filename: String, verbose: Boolean, goal: Int): Int =
        filename.asPath().parseTrack(verbose).findCheats(goal, 20)

    private fun List<Coordinate>.findCheats(goal: Int, cheats: Int): Int =
        indices.sumOf { start ->
            (start + goal..lastIndex).count { end ->
                val physicalDistance = this[start] manhattanDistance this[end]
                physicalDistance <= cheats && physicalDistance <= end - start - goal
            }
        }

    private fun Path.parseTrack(verbose: Boolean): List<Coordinate> {
        val map = readText().parseMatrix()

        val end = map.first { it == 'E' }
        return buildList {
            add(map.first { it == 'S' })
            while (last() != end) {
                add(
                    map.getOrthogonalNeighbors(last())
                        .filterNot { map[it] == '#' }
                        .first { it != getOrNull(lastIndex - 1) }
                )
            }
        }.apply {
            if (verbose) {
                map.print { it in this }
            }
        }
    }

    companion object : Day.Main("Day20.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}