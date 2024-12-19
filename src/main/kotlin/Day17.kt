import java.nio.file.Path
import kotlin.io.path.readLines

class Day17 : Day {

    override fun partOne(filename: String, verbose: Boolean): String {
        val program = filename.asPath().parseProgram()
        val result = program.execute()
        return result.joinToString(",")
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        val program = filename.asPath().parseProgram().copy(registerA = 0, registerB = 0, registerC = 0)
        return program.findA()!!
    }

    private fun Program.execute(): List<Int> {
        var a = registerA
        var b = registerB
        var c = registerC

        val combo = { operand: Int ->
            when (operand) {
                in 0..3 -> operand.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> error("Invalid operand: $operand")
            }
        }

        val out = mutableListOf<Int>()

        var pc = 0
        while (pc in program.indices) {
            val opcode = program[pc]
            val operand = program[pc + 1]

            when (opcode) {
                0 -> a = a shr combo(operand).toInt()
                1 -> b = b xor operand.toLong()
                2 -> b = combo(operand) % 8
                3 -> if (a != 0L) {
                    pc = operand
                    continue
                }

                4 -> b = b xor c
                5 -> out += (combo(operand) % 8).toInt()
                6 -> b = a shr combo(operand).toInt()
                7 -> c = a shr combo(operand).toInt()
            }
            pc += 2
        }

        return out
    }

    private fun Program.findA(): Long? =
        (registerA..registerA + 8).firstNotNullOfOrNull { a ->
            val out = copy(registerA = a).execute()

            if (program.takeLast(out.size) == out) {
                if (program == out) a else copy(registerA = maxOf(a shl 3, 8)).findA()
            } else {
                null
            }
        }

    private data class Program(
        val registerA: Long,
        val registerB: Long,
        val registerC: Long,
        val program: List<Int>,
    )

    private fun Path.parseProgram() =
        readLines().let { lines ->
            Program(
                lines[0].substringAfter("A: ").toLong(),
                lines[1].substringAfter("B: ").toLong(),
                lines[2].substringAfter("C: ").toLong(),
                lines[4].substringAfter(": ").split(",").map { it.toInt() }
            )
        }

    companion object : Day.Main("Day17.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}