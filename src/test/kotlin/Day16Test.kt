import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test : DayTest<Day16>("Day16_test.txt") {
    override val partOneExpected = 7036
    override val partTwoExpected = 45

    private val secondExampleFilename = "Day16_test_second.txt"

    @Test
    fun `Part One - second example`() {
        val expected = 11048

        val result = target.partOne(secondExampleFilename, true)

        assertEquals(expected, result)
    }

    @Test
    fun `Part Two - second example`() {
        val expected = 64

        val result = target.partTwo(secondExampleFilename, true)

        assertEquals(expected, result)
    }
}