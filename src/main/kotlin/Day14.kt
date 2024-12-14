import java.nio.file.Path
import kotlin.io.path.readLines

class Day14 : Day {
    private val format = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")

    override fun partOne(filename: String, verbose: Boolean): Int {
        return partOne(filename, verbose, 101, 103)
    }

    internal fun partOne(filename: String, verbose: Boolean, maxX: Int, maxY: Int): Int {
        val robots = filename.asPath().parseRobots()
        if (verbose) {
            robots.forEach {
                println(it)
            }
        }
        repeat(100) {
            robots.move(maxX, maxY)
        }

        return robots.safetyFactor(maxX, maxY)
    }

    override fun partTwo(filename: String, verbose: Boolean): Any {
        val maxX = 101
        val maxY = 103
        val robots = filename.asPath().parseRobots()
        var seconds = 0
        var christmasTree: Pair<Int, List<Robot>>? = null
        var minEntropy = robots.entropy()
        val seen = mutableSetOf<Long>()
        while (true) {
            robots.move(maxX, maxY)
            seconds++
            val entropy = robots.entropy()
            if (entropy < minEntropy) {
                minEntropy = entropy
                christmasTree = seconds to robots.toList()
            }
            if (!seen.add(entropy) && seconds > 10_000) {
                break
            }
        }
        val (time, tree) = christmasTree ?: error("Tree not found")
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val coordinate = Coordinate(x, y)
                val count = tree.count { (position) -> position == coordinate }
                if(count == 0) {
                    print(".")
                } else {
                    print(count.toString(32))
                }
            }
            println()
        }
        return time
    }

    private fun Path.parseRobots() =
        readLines()
            .mapNotNull { format.matchEntire(it)?.destructured }
            .map { (pX, pY, vX, vY) -> Robot(Coordinate(pX.toInt(), pY.toInt()), Coordinate(vX.toInt(), vY.toInt())) }
            .toMutableList()

    private fun MutableList<Robot>.move(maxX: Int, maxY: Int) {
        replaceAll { (position, velocity) ->
            var newPosition = position + velocity
            when {
                newPosition.x >= maxX -> {
                    newPosition -= Coordinate(maxX, 0)
                }

                newPosition.x < 0 -> {
                    newPosition += Coordinate(maxX, 0)
                }
            }
            when {
                newPosition.y >= maxY -> {
                    newPosition -= Coordinate(0, maxY)
                }

                newPosition.y < 0 -> {
                    newPosition += Coordinate(0, maxY)
                }
            }
            Robot(newPosition, velocity)
        }
    }

    private fun List<Robot>.safetyFactor(maxX: Int, maxY: Int): Int {
        val quadrantCounts = Array(4) { 0 }
        forEach { (position, _) ->
            when {
                position.x < maxX / 2 && position.y < maxY / 2 -> {
                    quadrantCounts[0]++
                }
                position.x > maxX / 2 && position.y < maxY / 2 -> {
                    quadrantCounts[1]++
                }
                position.x < maxX / 2 && position.y > maxY / 2 -> {
                    quadrantCounts[2]++
                }
                position.x > maxX / 2 && position.y > maxY / 2 -> {
                    quadrantCounts[3]++
                }
            }
        }
        return quadrantCounts.fold(1) { product, it -> product * it }
    }

    private fun List<Robot>.entropy(): Long {
        var result = 0L
        for (i in this.indices) {
            for (j in i + 1 until this.size) {
                val distance = this[i].position manhattanDistance this[j].position
                result += distance
            }
        }
        return result
    }

    private data class Robot(val position: Coordinate, val velocity: Coordinate)

    companion object : Day.Main("Day14.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}