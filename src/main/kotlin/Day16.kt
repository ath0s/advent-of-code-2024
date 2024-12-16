import java.util.PriorityQueue

class Day16 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val map = filename.asPath().parseMatrix()

        return map.solve().minCost
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val map = filename.asPath().parseMatrix()

        return map.solve().bestSeats
    }

    private fun Matrix<Char>.solve(): Solution {
        val start = first { it == 'S' }
        val end = first { it == 'E' }

        var minTotalCost = Int.MAX_VALUE
        val bestSeats = mutableSetOf<Coordinate>()
        val seen = mutableMapOf<Pair<Coordinate, Direction>, Int>()

        val queue = PriorityQueue(compareBy(Step::cost))
        queue.add(Step(Direction.EAST, 0, listOf(start)))

        while (queue.isNotEmpty()) {
            queue
                .poll()
                .takeIf { (orientation, cost, path) ->
                    val node = path.last() to orientation
                    val bestCost = seen.getOrDefault(node, Int.MAX_VALUE)
                    if (cost < bestCost) {
                        seen[node] = cost
                    }

                    if (path.last() == end) {
                        if (cost > minTotalCost) {
                            return Solution(minTotalCost, bestSeats.size)
                        }
                        minTotalCost = cost
                        bestSeats += path
                    }

                    cost <= bestCost
                }
                ?.let { adjacentNodes(it) }
                ?.let(queue::addAll)
        }
        throw IllegalStateException("Path not found")
    }

    private fun Matrix<Char>.adjacentNodes(step: Step): List<Step> =
        Direction.CARDINAL
            .filterNot { it == step.direction.inverse() }
            .map { it to (step.path.last() move it) }
            .filterNot { (_, coordinate) -> getValue(coordinate) == '#' }
            .map { (direction, coordinate) ->
                Step(
                    direction,
                    step.cost + if (direction == step.direction) 1 else 1001,
                    step.path + coordinate
                )
            }

    private data class Step(val direction: Direction, val cost: Int, val path: List<Coordinate>)

    private data class Solution(val minCost: Int, val bestSeats: Int)

    companion object : Day.Main("Day16.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}