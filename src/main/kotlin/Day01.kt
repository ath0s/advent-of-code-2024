import java.nio.file.Path
import kotlin.io.path.readLines

class Day01 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long {
        val (left, right) = filename.asPath().parseLists()
        left.sort()
        right.sort()
        if (verbose) {
            println("left=$left")
            println("right=$right")
        }
        val sum = left.zip(right).fold(0L) { sum, (l, r) ->
            sum + (maxOf(l, r) - minOf(l, r))
        }
        return sum
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        val (left, right) = filename.asPath().parseLists()
        return left.fold(0L) { sum , l ->
            val count = right.count { it == l }
            sum + (l * count)
        }
    }

    private fun Path.parseLists(): Pair<MutableList<Long>, MutableList<Long>> {
        val left = mutableListOf<Long>()
        val right = mutableListOf<Long>()
        readLines().forEach { line ->
            val (l, r) = line.split(Regex("""\s+"""))
            left += l.toLong()
            right += r.toLong()
        }
        return left to right
    }

    companion object : Day.Main("Day01.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}