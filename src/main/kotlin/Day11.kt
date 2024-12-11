import java.nio.file.Path
import kotlin.io.path.readText

class Day11 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long {
        var stones = filename.asPath().parseStones()
        if (verbose) {
            println(stones.keys.joinToString(" "))
        }
        repeat(25) {
            stones = stones.blink()
        }

        return stones.values.sum()
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        var stones = filename.asPath().parseStones()

        repeat(75) {
            stones = stones.blink()
        }

        return stones.values.sum()
    }

    private fun Path.parseStones() =
        readText().split(" ").associateWith { 1L }

    private fun Map<String, Long>.blink() =
        let { stones ->
            buildMap<String, Long> {
                stones.forEach { (stone, count) ->
                    fun add(newStone: String) =
                        compute(newStone) { _, previous -> previous?.let { it + count } ?: count }
                    when {
                        stone == "0" -> add("1")
                        stone.length % 2 == 0 -> {
                            val mid = stone.length / 2
                            add(stone.substring(0, mid))
                            add(stone.substring(mid).toLong().toString())
                        }

                        else -> add((stone.toLong() * 2024L).toString())
                    }
                }
            }
        }

    companion object : Day.Main("Day11.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}