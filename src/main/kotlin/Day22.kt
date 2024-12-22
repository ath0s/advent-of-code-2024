import java.nio.file.Path
import kotlin.io.path.readLines

class Day22 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long =
        filename.asPath().parseSecretNumbers().sumOf { line ->
            var result = line
            repeat(2000) {
                result = result.next()
            }
            result
        }

    override fun partTwo(filename: String, verbose: Boolean): Int {
        val secretNumbers = filename.asPath().parseSecretNumbers()
        val sequences = mutableMapOf<List<Int>, Int>()
        secretNumbers.forEach { line ->
            var previous = line
            val seen = mutableSetOf<List<Int>>()
            (0..<2000)
                .asSequence()
                .map { previous.next().also { previous = it } }
                .map { (it % 10).toInt() }
                .windowed(5)
                .forEach { prices ->
                    val sequence = prices
                        .zipWithNext()
                        .map { (first, second) -> second - first }
                    if (seen.add(sequence)) {
                        sequences.compute(sequence) { _, v -> (v ?: 0) + prices.last() }
                    }
                }
        }

        return sequences.maxOf { it.value }
    }

    private fun Path.parseSecretNumbers() =
        readLines().map { it.toLong() }

    private fun Long.next() =
        step { it shl 6 }.step { it shr 5 }.step { it shl 11 }

    private fun Long.step(transform: (Long) -> Long) =
        (transform(this) xor this) and 0xFFFFFF

    companion object : Day.Main("Day22.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}