import Direction.EAST
import Direction.NORTH
import Direction.NORTH_EAST
import Direction.NORTH_WEST
import Direction.SOUTH
import Direction.SOUTH_EAST
import Direction.SOUTH_WEST
import Direction.WEST
import java.nio.file.Path
import kotlin.io.path.readText

private val DIRECTIONS = mapOf(
    '>' to EAST,
    'v' to SOUTH,
    '<' to WEST,
    '^' to NORTH,
)

private val NEIGHBOURS = mapOf(
    ('[' to NORTH) to NORTH_EAST,
    ('[' to SOUTH) to SOUTH_EAST,
    (']' to NORTH) to NORTH_WEST,
    (']' to SOUTH) to SOUTH_WEST,
)

class Day15 : Day {

    override fun partOne(filename: String, verbose: Boolean): Int {
        return filename.asPath().parseInput(isWide = false)
            .let { (map, moves) ->
                var robotPosition = map.filterValues { it == '@' }.keys.first()

                moves.forEach { move ->
                    val q = mutableListOf<Coordinate>()
                    val direction = DIRECTIONS[move] ?: throw Exception("unknown move $move")
                    var currentPosition = robotPosition

                    q.add(currentPosition)

                    while (true) {
                        val nextPosition = currentPosition + direction
                        val next = map[nextPosition]
                        q.add(nextPosition)

                        when (next) {
                            '#' -> {
                                // hit a wall - can't move anything
                                q.clear()
                                break
                            }

                            '.' -> {
                                // found a gap -> can move everything
                                break
                            }

                            'O' -> {
                                // found a box -> try to push it
                            }

                            else -> {
                                throw Exception("unknown next $next")
                            }
                        }

                        currentPosition = nextPosition
                    }

                    q.reversed().zipWithNext().forEach { (next, current) ->
                        val temp = map[next] ?: throw Exception("undefined map position $next")
                        map[next] = map[current] ?: throw Exception("undefined map position $current")
                        map[current] = temp
                    }

                    if (q.isNotEmpty()) {
                        robotPosition += direction
                    }
                }

                map.filterValues { it == 'O' }.keys.sumOf { it.y * 100 + it.x }
            }

    }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        return filename.asPath().parseInput(isWide = true)
            .let { (map, moves) ->
                var robotPosition = map.filterValues { it == '@' }.keys.first()

                moves.forEach { move ->
                    val currentPosition = robotPosition
                    val direction = DIRECTIONS[move] ?: throw Exception("unknown move $move")

                    val affected = mutableSetOf<Pair<Coordinate, Coordinate>>()
                    val canMove = findAllBlocking(map, currentPosition, direction, affected)

                    if (canMove) {
                        // remove old robot
                        map[robotPosition] = '.'

                        // remove old boxes
                        affected.forEach { (p1, p2) ->
                            map[p1] = '.'
                            map[p2] = '.'
                        }

                        // move robot
                        robotPosition += direction
                        map[robotPosition] = '@'

                        // move boxes
                        affected.forEach { (p1, p2) ->
                            map[p1 + direction] = '['
                            map[p2 + direction] = ']'
                        }
                    }
                }

                map.filterValues { it == '[' }.keys.sumOf { it.y * 100 + it.x }
            }

    }

    private fun findPair(map: Map<Coordinate, Char>, position: Coordinate) =
        when (val item = map[position]) {
            '[' -> position to position + Coordinate(1, 0)
            ']' -> position + Coordinate(-1, 0) to position
            else -> throw Exception("unsupported item $item")
        }

    private fun findAllBlocking(
        map: Map<Coordinate, Char>,
        currentPosition: Coordinate,
        direction: Direction,
        affected: MutableSet<Pair<Coordinate, Coordinate>>
    ): Boolean {
        val nextPosition = currentPosition + direction

        when (val next = map[nextPosition]) {
            '#' -> {
                // found a wall -> cannot move
                return false
            }

            '.' -> {
                // found a gap -> can move
                return true
            }

            else -> {
                // found a box -> add it to affected and along with its neighbours
                affected.add(findPair(map, nextPosition))

                val neighbour = NEIGHBOURS[next to direction]

                val others = if (neighbour != null) {
                    affected.add(findPair(map, currentPosition + neighbour))
                    findAllBlocking(map, currentPosition + neighbour, direction, affected)
                } else {
                    true
                }

                return findAllBlocking(map, nextPosition, direction, affected) && others
            }
        }
    }

    private fun Path.parseInput(isWide: Boolean) = readText()
        .split("\n\n")
        .let { (board, moves) ->
            val map = mutableMapOf<Coordinate, Char>()

            board
                .let {
                    if (isWide) {
                        it
                            .replace("#", "##")
                            .replace("O", "[]")
                            .replace(".", "..")
                            .replace("@", "@.")
                    } else {
                        it
                    }
                }
                .lines().forEachIndexed { row, line ->
                    line.forEachIndexed { col, symbol ->
                        map[Coordinate(col, row)] = symbol
                    }
                }

            map to moves.filterNot { it == '\n' }
        }

    companion object : Day.Main("Day15.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}