import java.nio.file.Path
import kotlin.io.path.readLines

class Day19 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val (words, patterns) = filename.asPath().parseWordsAndPatterns()
        return patterns.count { it.countOptions(words) > 0 }
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        val (words, patterns) = filename.asPath().parseWordsAndPatterns()
        return patterns.sumOf { it.countOptions(words) }
    }

    private fun Path.parseWordsAndPatterns() =
        readLines().let { lines ->
            val words = lines.first().split(", ")
            val patterns = lines.drop(2)
            words to patterns
        }

    private fun String.countOptions(towels: List<String>): Long {
        val options = LongArray(length + 1).also { it[length] = 1 }

        indices.reversed().forEach { index ->
            towels.forEach { towel ->
                if (startsWith(towel, index)) {
                    options[index] += options[index + towel.length]
                }
            }
        }

        return options[0]
    }

    companion object : Day.Main("Day19.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}