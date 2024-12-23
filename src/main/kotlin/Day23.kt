import java.nio.file.Path
import java.util.SortedSet
import java.util.TreeSet
import kotlin.io.path.readLines

class Day23 : Day {
    override fun partOne(filename: String, verbose: Boolean): Int {
        val connections = filename.asPath().parseConnections()

        val triplets = connections.findGroups(3)
            .filter { it.size == 3 }

        return triplets.count { triplet -> triplet.any { it.startsWith('t') } }
    }

    override fun partTwo(filename: String, verbose: Boolean): String {
        val connections = filename.asPath().parseConnections()

        val triplets = connections.findGroups()

        return triplets.maxBy { it.size }.joinToString(",")
    }

    private fun Path.parseConnections() =
        buildMap {
            readLines().forEach {
                val (from, to) = it.split("-")
                compute(from) { _, connections: MutableSet<String>? -> connections?.apply { add(to) } ?: mutableSetOf(to) }
                compute(to) { _, connections: MutableSet<String>? -> connections?.apply { add(from) } ?: mutableSetOf(from) }
            }
        }

    private fun Map<String, Set<String>>.findGroups(
        limit: Int = Int.MAX_VALUE,
    ): Set<SortedSet<String>> = buildSet {
        fun recurse(node: String, group: SortedSet<String>) {
            group.takeUnless { it in this }
                ?.also(::add)
                ?.takeIf { it.size < limit }
                ?: return

            for (neighbor in getValue(node)) {
                if (neighbor in group) continue
                if (!getValue(neighbor).containsAll(group)) continue
                recurse(neighbor, group + neighbor)
            }
        }

        for (x in keys) {
            recurse(x, sortedSetOf(x))
        }
    }

    private operator fun <T> SortedSet<T>.plus(element: T): SortedSet<T> =
        TreeSet(this).apply { add(element) }

    companion object : Day.Main("Day23.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}