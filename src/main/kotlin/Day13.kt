import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.text.RegexOption.MULTILINE

class Day13 : Day {
    private val format = Regex("""Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""", MULTILINE)

    override fun partOne(filename: String, verbose: Boolean): Long {
        val machines = filename.asPath().parseMachines()

        if (verbose) {
            machines.forEach {
                println(it)
                println()
            }
        }

        return machines.sumOf { it.pushButtons() }
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        val machines = filename.asPath().parseMachines()
            .map { it.copy(prize = it.prize + LongCoordinate(10000000000000, 10000000000000)) }

        if (verbose) {
            machines.forEach {
                println(it)
                println()
            }
        }

        return machines.sumOf { it.pushButtons() }
    }

    private fun Path.parseMachines() =
        format.findAll(readText()).map {
            val (buttonAx, buttonAy, buttonBx, buttonBy, prizeX, prizeY) = it.destructured
            Machine(
                buttonA = LongCoordinate(buttonAx.toLong(), buttonAy.toLong()),
                buttonB = LongCoordinate(buttonBx.toLong(), buttonBy.toLong()),
                prize = LongCoordinate(prizeX.toLong(), prizeY.toLong())
            )
        }.toList()

    private data class Machine(
        val buttonA: LongCoordinate,
        val buttonB: LongCoordinate,
        val prize: LongCoordinate
    ) {
        fun pushButtons(): Long {
            val det = buttonA.x * buttonB.y - buttonA.y * buttonB.x
            val a = (prize.x * buttonB.y - prize.y * buttonB.x) / det
            val b = (buttonA.x * prize.y - buttonA.y * prize.x) / det
            return if (buttonA.x * a + buttonB.x * b == prize.x && buttonA.y * a + buttonB.y * b == prize.y) {
                a * 3 + b
            } else 0
        }

        override fun toString() =
            """
            Button A: X+${buttonA.x}, Y+${buttonA.y}
            Button B: X+${buttonB.x}, Y+${buttonB.y}
            Prize: X=${prize.x}, Y=${prize.y}
        """.trimIndent()
    }

    companion object : Day.Main("Day13.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}