import Day07.Operator.ADD
import Day07.Operator.CONCAT
import Day07.Operator.MULTIPLY
import java.nio.file.Path
import kotlin.io.path.readLines

class Day07 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long {
        val equations = filename.asPath().parse()

        return equations.resolve(setOf(ADD, MULTIPLY))
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val equations = filename.asPath().parse()

        return equations.resolve(setOf(ADD, MULTIPLY, CONCAT))
    }

    private fun Path.parse(): List<Pair<Long, List<Long>>> {
        return readLines()
            .map<String, Pair<Long, List<Long>>> {
                val numbers = Regex("""(\d+)""").findAll(it).map { it.groupValues[1].toLong() }
                numbers.first().toLong() to numbers.drop(1).toList()
            }
    }

    private fun List<Pair<Long, List<Long>>>.resolve(operators: Set<Operator>): Long {
        var sum = 0L
        this.forEach { (testValue, numbers) ->
            var values = listOf(numbers.first())
            numbers.drop(1).forEach { number ->
                values = buildList {
                    values.forEach { value ->
                        operators.forEach { operator ->
                            add(operator(value, number))
                        }
                    }
                }
            }
            if (testValue in values) {
                sum += testValue
            }
        }


        return sum
    }

    private enum class Operator(private val operation: (Long, Long) -> Long) {
        ADD(Long::plus),
        MULTIPLY(Long::times),
        CONCAT({ a, b -> "$a$b".toLong() });

        operator fun invoke(a: Long, b: Long) =
            operation(a, b)
    }


    companion object : Day.Main("Day07.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}

