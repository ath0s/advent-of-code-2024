import Day02.Direction.ASCENDING
import Day02.Direction.DESCENDING
import kotlin.io.path.readLines

class Day02 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val reports = filename.asPath().readLines()
            .map { line -> line.split(" ").map { it.toInt() } }
        if (verbose) {
            reports.forEach {
                println(it.joinToString(" "))
            }
        }
        val numberOfValidReports = reports.count { it.isValid() }

        return numberOfValidReports
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        TODO("Not yet implemented")
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

    private enum class Direction(val valid: IntRange) {
        ASCENDING(1..3),
        DESCENDING(-3..-1)
    }

    companion object : Day.Main("Day02.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}