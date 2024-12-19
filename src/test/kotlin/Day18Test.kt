import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test : DayTest<Day18>("Day18_test.txt") {
    override val partOneExpected = 22
    override val partTwoExpected = "6,1"

    @Test
    override fun `Part One`() {
        assumeNotNull(filenamePartOne)

        val result = target.partOne(filenamePartOne, Coordinate(6,6), 12)

        assertEquals(partOneExpected, result)
    }

    @Test
    override fun `Part Two`() {
        assumeNotNull(filenamePartOne)

        val result = target.partTwo(filenamePartOne, Coordinate(6,6))

        assertEquals(partTwoExpected, result)
    }
}