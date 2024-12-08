class Day08 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val matrix = filename.asPath().parseMatrix()
        val antennas = matrix.findAntennas()
        val antiNodes = matrix.antiNodes(antennas, Matrix<Char>::twoAntiNodes)
        return antiNodes.size
    }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        val matrix = filename.asPath().parseMatrix()
        val antennas = matrix.findAntennas()
        val antiNodes = matrix.antiNodes(antennas, Matrix<Char>::allAntiNodes)
        return antiNodes.size
    }

    private fun Matrix<Char>.findAntennas() =
        let { matrix ->
            buildMap {
                matrix.forEachIndexed { coordinate, ch ->
                    if (ch != '.') {
                        computeIfAbsent(ch) { mutableListOf<Coordinate>() } += coordinate
                    }
                }
            }
        }

    private fun Matrix<Char>.antiNodes(antennas: Map<Char, List<Coordinate>>, createAntiNodes: (Matrix<Char>.(Coordinate, Coordinate) -> Set<Coordinate>)): Set<Coordinate> =
        antennas.values.flatMapToSet { coordinates ->
            coordinates.allPairs().flatMap { (first, second) -> createAntiNodes(first, second) }
        }


    companion object : Day.Main("Day08.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}

private fun Matrix<Char>.twoAntiNodes(first: Coordinate, second: Coordinate): Set<Coordinate> =
    listOf(
        first + (second - first) * 2,
        second + (first - second) * 2
    ).filterToSet { it in this }

private fun Matrix<Char>.allAntiNodes(first: Coordinate, second: Coordinate): Set<Coordinate> =
    let { matrix ->
        buildSet {
            addAll(generateSequence(second) { current -> (current + (second - first)).takeIf { it in matrix } })
            addAll(generateSequence(first) { current -> (current + (first - second)).takeIf { it in matrix } })
        }
    }
