import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Test : DayTest<Day20>("Day20_test.txt") {
    override val partOneExpected = 2
    override val partTwoExpected = 7

    @Test
    override fun `Part One`() {
        assumeNotNull(filenamePartOne)

        val result = target.partOne(filenamePartOne, true, 40)

        assertEquals(partOneExpected, result)
    }

    @Test
    override fun `Part Two`() {
        assumeNotNull(filenamePartOne)

        val result = target.partTwo(filenamePartOne, true, 74)

        assertEquals(partTwoExpected, result)
    }
}