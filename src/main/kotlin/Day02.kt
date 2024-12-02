import Day02.Direction.ASCENDING
import Day02.Direction.DESCENDING
import java.nio.file.Path
import kotlin.io.path.readLines

class Day02 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val reports = filename.asPath().parseReports(verbose)

        val numberOfValidReports = reports.count { it.isValid() }

        return numberOfValidReports
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val reports = filename.asPath().parseReports(verbose)

        val numberOfValidReports = reports.count { report -> report.permutations().any { it.isValid() } }

        return numberOfValidReports
    }

    private fun Path.parseReports(verbose: Boolean): List<List<Int>> {
        val reports = readLines()
            .map { line -> line.split(" ").map { it.toInt() } }
        if (verbose) {
            reports.forEach {
                println(it.joinToString(" "))
            }
        }
        return reports
    }

    private fun List<Int>.isValid(): Boolean {
        var direction: Direction? = null
        windowed(2).forEach { (first, second) ->
            val diff = second - first
            if (direction == null) {
                direction = when (diff) {
                    in ASCENDING.valid -> ASCENDING
                    in DESCENDING.valid -> DESCENDING
                    else -> return false
                }
            } else {
                if (diff !in direction.valid) {
                    return false
                }
            }
        }
        return true
    }

    private fun List<Int>.permutations(): Sequence<List<Int>> = sequence {
        val initialList = this@permutations
        yield(initialList)
        for (listIndex in initialList.indices) {
            yield(initialList.filterIndexed { index, _ -> index != listIndex })
        }
    }

    private enum class Direction(val valid: IntRange) {
        ASCENDING(1..3),
        DESCENDING(-3..-1)
    }

    companion object : Day.Main("Day02.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}