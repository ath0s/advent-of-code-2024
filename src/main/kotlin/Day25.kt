import java.nio.file.Path
import kotlin.io.path.readText

class Day25: Day {
    override fun partOne(filename: String, verbose: Boolean): Any {
        val (locks, keys) = filename.asPath().parseLocksAndKeys()
        return locks.flatMap { lock ->
            keys.map { key -> lock.zip(key).map { (a, b) -> a + b }.all { it <= 7 } }
        }.count { it }
    }

    override fun partTwo(filename: String, verbose: Boolean) {

    }

    private fun Path.parseLocksAndKeys() =
        readText().split("\n\n").map { block ->
            block.lines().map { s -> s.map { if (it == '#') 1 else 0 } }
                .reduce { a, b -> a.zip(b) { first, second -> first + second } } to block.first()
        }.groupBy({ (_, it) -> it }, { (it) -> it}).values.toList()

    companion object : Day.Main("Day25.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}