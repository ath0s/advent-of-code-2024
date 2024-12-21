import java.nio.file.Path
import kotlin.io.path.readLines

class Day21 : Day {
    private val mapDir = arrayOf(
        arrayOf('X', '^', 'A'),
        arrayOf('<', 'v', '>'),
    )
    private val stepsDir = buildMap {
        "^A<v>".forEach { start ->
            "^A<v>".forEach { end ->
                this[start to end] = generateSteps(mapDir, start, end)
            }
        }
    }
    private val mapNum = arrayOf(
        arrayOf('7', '8', '9'),
        arrayOf('4', '5', '6'),
        arrayOf('1', '2', '3'),
        arrayOf('X', '0', 'A'),
    )

    override fun partOne(filename: String, verbose: Boolean): Long =
        filename.asPath().calculateComplexity(3)

    override fun partTwo(filename: String, verbose: Boolean): Long =
        filename.asPath().calculateComplexity(26)

    private fun Path.calculateComplexity(levels: Int): Long {
        val stepsNum = buildMap {
            for (start in "7894561230A") {
                for (end in "7894561230A") {
                    this[start to end] = generateSteps(mapNum, start, end)
                }
            }
        }

        val cache = mutableMapOf<State, Long>()

        var sum = 0L
        readLines().forEach { code ->
            var value = 0L
            var previous = 'A'
            for (current in code) {
                value += stepsNum.numberOfPresses(cache, State(previous, current, levels))
                previous = current
            }
            val complexity = value * code.substring(0, code.length - 1).toLong()
            sum += complexity
        }
        return sum
    }

    private fun Map<Pair<Char, Char>, List<String>>.numberOfPresses(cache: MutableMap<State, Long>, state: State): Long {
        cache[state]?.let { return it }

        if (state.remainingLevels == 0) {
            return 1
        }

        var best = Long.MAX_VALUE
        for (steps in this[state.previous to state.current]!!) {
            var value = 0L
            var previous = 'A'
            for (current in steps) {
                value += stepsDir.numberOfPresses(cache, State(previous, current, state.remainingLevels - 1))
                previous = current
            }
            best = minOf(value, best)
        }
        cache[state] = best
        return best
    }

    private fun generateSteps(map: Matrix<Char>, start: Char, end: Char, visited: Set<Char> = emptySet()): List<String> {
        if (start == end) {
            return listOf("A")
        }
        val startCoordinate = map.first { it == start }
        return buildList {
            Direction.CARDINAL.forEach { direction ->
                val next = map.getValue(startCoordinate + direction)
                if (
                    next != null &&
                    next != 'X' &&
                    next !in visited
                ) {
                    addAll(
                        generateSteps(map, next, end, visited + start).map { direction.char + it }
                    )
                }
            }
        }
    }

    data class State(val previous: Char, val current: Char, val remainingLevels: Int)

    private val Direction.char
        get() = when (this) {
            Direction.NORTH -> '^'
            Direction.EAST -> '>'
            Direction.SOUTH -> 'v'
            Direction.WEST -> '<'
            else -> '.'
        }

    companion object : Day.Main("Day21.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}