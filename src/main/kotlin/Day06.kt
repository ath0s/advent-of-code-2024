class Day06 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val map = filename.asPath().parseMatrix()
        var guard = map.find { ch -> ch != '.' && ch != '#' } ?: throw IllegalStateException("Guard not found")
        var direction = Direction.NORTH
        val positions = map.walk(guard to direction)
        val visited = positions!!.mapToSet { (coordinate) -> coordinate }
        if (verbose) {
            map.print('X') { it in visited }
            println()
        }

        return visited.size
    }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        val map = filename.asPath().parseMatrix()
        val start = map.find { ch -> ch != '.' && ch != '#' } ?: throw IllegalStateException("Guard not found")
        return map.walk(start to Direction.NORTH)!!
            .filter { (position) -> position != start }
            .distinctBy { (position) -> position }
            .count { (position, direction) ->
                val path = map.walk((position move direction.inverse()) to direction, position)
                val loop = path == null
                if (verbose && loop) {
                    map.print('O') { it == position }
                    println()
                }
                loop
            }
    }

    private fun Matrix<Char>.walk(start: Pair<Coordinate, Direction>, obstruction: Coordinate? = null): Set<Pair<Coordinate, Direction>>? {
        val map = this
        var (guard, direction) = start
        return buildSet {
            while (guard in map) {
                if (!add(guard to direction)) {
                    return null
                }

                val next = guard move direction
                if (next in map && (next == obstruction || map[next] == '#')) {
                    direction = direction.turn90DegreesRight()
                } else {
                    guard = next
                }
            }
        }
    }

    companion object : Day.Main("Day06.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}