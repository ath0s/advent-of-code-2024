import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test : DayTest<Day14>("Day14_test.txt") {
    override val partOneExpected = 12
    override val partTwoExpected = null

    @Test
    override fun `Part One`() {
        assumeNotNull(filenamePartOne)

        val result = target.partOne(filenamePartOne, true, 11, 7)

        assertEquals(partOneExpected, result)
    }
}