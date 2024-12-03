import kotlin.io.path.readText

class Day03 : Day {
    private val mulPattern = Regex("""mul\((\d+),(\d+)\)""")
    private val mulDontDoPattern = Regex("""mul\((\d+),(\d+)\)|don't|do""")

    override fun partOne(filename: String, verbose: Boolean): Long {
        val input = filename.asPath().readText()
        return mulPattern.findAll(input).map { result ->
            val (multiplier, multiplicand) = result.destructured
            multiplier.toLong() * multiplicand.toLong()
        }.sum()
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val input = filename.asPath().readText()
        var enabled = true
        return mulDontDoPattern.findAll(input).map { result ->
            val (instruction, multiplier, multiplicand) = result.groupValues
            var value = 0L
            when (instruction) {
                "don't" -> enabled = false
                "do" -> enabled = true
                else -> if (enabled) {
                    value = multiplier.toLong() * multiplicand.toLong()
                }
            }
            value
        }.sum()
    }

    companion object : Day.Main("Day03.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}