import kotlin.io.path.readLines

class Day05 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val (rules, updates) = parseRulesAndUpdates(filename)

        return updates.filter { update ->
            val sorted = update.sortedWith { first, second ->
                when {
                    first to second in rules -> -1
                    second to first in rules -> 1
                    else -> 0
                }
            }
            sorted == update
        }.sumOf { update ->
            update[update.size / 2]
        }
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val (rules, updates) = parseRulesAndUpdates(filename)

        return updates.mapNotNull { update ->
            val sorted = update.sortedWith { first, second ->
                when {
                    first to second in rules -> -1
                    second to first in rules -> 1
                    else -> 0
                }
            }
            sorted.takeIf { it != update }
        }.sumOf { update ->
            update[update.size / 2]
        }
    }

    private fun parseRulesAndUpdates(filename: String): Pair<Set<Pair<Int, Int>>, List<List<Int>>> {
        val lines = filename.asPath().readLines()
        val separator = lines.indexOfFirst { it.isBlank() }
        val rules = lines.take(separator).mapToSet { line ->
            val (left, right) = line.split("|").map { it.toInt() }
            left to right
        }
        val updates = lines.drop(separator + 1).map { line ->
            line.split(",").map { it.toInt() }
        }
        return Pair(rules, updates)
    }

    companion object : Day.Main("Day05.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}