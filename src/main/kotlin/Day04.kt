import Direction.NORTH_EAST
import Direction.NORTH_WEST
import Direction.SOUTH_EAST
import Direction.SOUTH_WEST
import kotlin.enums.enumEntries

class Day04 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val grid = filename.asPath().parseMatrix()
        val found = mutableSetOf<Set<Coordinate>>()
        grid.forEachIndexed { x, ch ->
            if (ch == 'X') {
                enumEntries<Direction>().forEach { dir ->
                    val m = x move dir
                    if (grid.getValue(m) == 'M') {
                        val a = m move dir
                        if (grid.getValue(a) == 'A') {
                            val s = a move dir
                            if (grid.getValue(s) == 'S') {
                                found += setOf(x, m, a, s)
                            }
                        }
                    }
                }
            }
        }

        if (verbose) {
            val matchedCoordinates = found.flattenToSet()
            grid.print { coordinate -> coordinate in matchedCoordinates }
        }

        return found.size
    }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        val grid = filename.asPath().parseMatrix()
        val found = mutableSetOf<Set<Coordinate>>()
        grid.forEachIndexed { a, ch ->
            if (ch == 'A') {
                val nw = a move NORTH_WEST
                val ne = a move NORTH_EAST
                val sw = a move SOUTH_WEST
                val se = a move SOUTH_EAST
                if ((grid.getValue(nw) == 'M' && grid.getValue(se) == 'S') || (grid.getValue(nw) == 'S' && grid.getValue(se) == 'M')) {
                    if ((grid.getValue(ne) == 'M' && grid.getValue(sw) == 'S') || (grid.getValue(ne) == 'S' && grid.getValue(sw) == 'M')) {
                        found += setOf(a, nw, ne, sw, se)
                    }
                }
            }
        }

        if (verbose) {
            val matchedCoordinates = found.flattenToSet()
            grid.print { coordinate -> coordinate in matchedCoordinates }
        }

        return found.size
    }

    companion object : Day.Main("Day04.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}