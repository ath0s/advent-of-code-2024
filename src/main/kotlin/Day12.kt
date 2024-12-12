import AnsiColor.BLACK_BACKGROUND
import AnsiColor.RESET
import Direction.EAST
import Direction.NORTH
import Direction.SOUTH
import Direction.WEST

class Day12 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val garden = filename.asPath().parseMatrix()
        val regions = garden.findRegions()
        return regions.sumOf { (type, region) ->
            val area = region.size
            val perimeter = region.sumOf { garden.countNeighborsOfDifferentType(it, garden[it]) }
            if (verbose) {
                println("A region of $BLACK_BACKGROUND$type$RESET plants with price $BLACK_BACKGROUND$area * $perimeter = ${area * perimeter}$RESET")
            }
            area * perimeter
        }
    }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        val garden = filename.asPath().parseMatrix()
        val regions = garden.findRegions()
        return regions.sumOf { (type, region) ->
            var numberOfLines = 0
            for (y in region.minOf { it.y } - 1..region.maxOf { it.y } + 1) {
                var bottomLineStart = false
                var topLineStart = false
                for (x in region.minOf { it.x } - 1..region.maxOf { it.x } + 1) {
                    val coordinate = Coordinate(x, y)
                    if (coordinate in region && garden.getValue(coordinate move NORTH) != type) {
                        bottomLineStart = true
                    } else if (coordinate in region && garden.getValue(coordinate move NORTH) == type || coordinate !in region) {
                        if (bottomLineStart) {
                            numberOfLines++
                        }
                        bottomLineStart = false
                    }
                    if (coordinate in region && garden.getValue(coordinate move SOUTH) != type) {
                        topLineStart = true
                    } else if (coordinate in region && garden.getValue(coordinate move SOUTH) == type || coordinate !in region) {
                        if (topLineStart) {
                            numberOfLines++
                        }
                        topLineStart = false
                    }
                }
            }
            for (x in region.minOf { it.x } - 1..region.maxOf { it.x } + 1) {
                var leftLineStart = false
                var rightLineStart = false
                for (y in region.minOf { it.y } - 1..region.maxOf { it.y } + 1) {
                    val coordinate = Coordinate(x, y)
                    if (coordinate in region && garden.getValue(coordinate move WEST) != type) {
                        leftLineStart = true
                    } else if (coordinate in region && garden.getValue(coordinate move WEST) == type || coordinate !in region) {
                        if (leftLineStart) {
                            numberOfLines++
                        }
                        leftLineStart = false
                    }
                    if (coordinate in region && garden.getValue(coordinate move EAST) != type) {
                        rightLineStart = true
                    } else if (coordinate in region && garden.getValue(coordinate move EAST) == type || coordinate !in region) {
                        if (rightLineStart) {
                            numberOfLines++
                        }
                        rightLineStart = false
                    }
                }
            }

            region.size * numberOfLines
        }
    }

    private fun Matrix<Char>.findRegions() =
        let { garden ->
            buildList {
                val visited = mutableSetOf<Coordinate>()
                garden.forEachIndexed { coordinate, type ->
                    if (coordinate !in visited) {
                        val region = garden.findNeighborsOfSameType(coordinate, type, visited)
                        add(type to region)
                    }
                }
            }
        }

    private fun Matrix<Char>.findNeighborsOfSameType(coordinate: Coordinate, type: Char, visited: MutableSet<Coordinate>): Set<Coordinate> {
        visited += coordinate
        return setOf(coordinate) + Direction.CARDINAL.asSequence()
            .map { coordinate move it }
            .filter { it !in visited }
            .filter { getValue(it) == type }
            .flatMap { findNeighborsOfSameType(it, type, visited) }
            .toSet()
    }

    private fun Matrix<Char>.countNeighborsOfDifferentType(coordinate: Coordinate, type: Char) =
        Direction.CARDINAL
            .map { coordinate move it }
            .count { getValue(it) != type }

    companion object : Day.Main("Day12.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}