class Day10 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val grid = filename.asPath().parseMatrix { it.digitToInt() }
        return grid.filterIndexed { _, value -> value == 0 }.sumOf { start ->
            val seen = mutableSetOf<Pair<Coordinate, Int>>()
            val trail = mutableListOf(start to grid[start])
            while (trail.isNotEmpty()) {
                val current = trail.removeFirst()
                seen += current
                val nextValue = current.second + 1
                val neighbors = grid.getOrthogonalNeighbors(current.first)
                    .map { it to grid[it] }
                    .filter { (_, neighborValue) -> neighborValue == nextValue }
                trail += neighbors

            }
            seen.count { (_, value) -> value == 9 }
        }
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val grid = filename.asPath().parseMatrix { it.digitToInt() }
        return grid.filterIndexed { _, value -> value == 0 }.sumOf { start ->
            val seen = mutableSetOf<Pair<Coordinate, Int>>()
            val trail = mutableListOf(start to grid[start])
            var count = 0
            while (trail.isNotEmpty()) {
                val current = trail.removeFirst()
                seen += current
                val nextValue = current.second + 1
                val neighbors = grid.getOrthogonalNeighbors(current.first)
                    .map { it to grid[it] }
                    .filter { (_, neighborValue) -> neighborValue == nextValue }
                trail += neighbors
                if (neighbors.isEmpty() && current.second == 9) {
                    count++
                }
            }
            count
        }
    }

    companion object : Day.Main("Day10.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}