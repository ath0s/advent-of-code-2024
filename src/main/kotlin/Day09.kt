import java.nio.file.Path
import kotlin.io.path.readText

class Day09 : Day {
    override fun partOne(filename: String, verbose: Boolean): Long {
        val blocks = filename.asPath().createBlocks()
        if (verbose) {
            blocks.print()
        }

        val compactedBlocks = moveBlocks(blocks)
        if (verbose) {
            compactedBlocks.print()
        }

        return calculateChecksum(compactedBlocks)
    }

    override fun partTwo(filename: String, verbose: Boolean): Long {
        val files = filename.asPath().createFiles()
        if (verbose) {
            files.expand().print()
        }

        val compactedFiles = moveFiles(files)
        if (verbose) {
            compactedFiles.expand().print()
        }

        return calculateChecksum(compactedFiles.expand())
    }

    private fun Path.createBlocks(): List<Int> {
        var id = 0
        val blocks = readText().flatMapIndexed { index, ch ->
            val digit = ch.digitToInt()
            if (index % 2 == 0) {
                (0..<digit).map { id }.also {
                    id++
                }
            } else {
                (0..<digit).map { EMPTY }
            }
        }
        return blocks
    }

    private fun Path.createFiles(): List<BlockType> {
        var id = 0
        val blocks = readText().mapIndexed { index, ch ->
            val length = ch.digitToInt()
            if (index % 2 == 0) {
                File(id++, length)
            } else {
                Empty(length)
            }
        }
        return blocks
    }

    private fun moveBlocks(blocks: List<Int>): List<Int> {
        val moved = blocks.toMutableList()
        var index = 0
        var endIndex = moved.lastIndex
        while (index < endIndex) {
            if (moved[index] == EMPTY) {
                while (moved[endIndex] == EMPTY) {
                    endIndex--
                }
                moved[index] = moved[endIndex]
                moved[endIndex] = EMPTY
            }
            index++
        }
        return moved
    }

    private fun moveFiles(files: List<BlockType>): List<BlockType> {
        val moved = files.toMutableList()
        var endIndex = moved.lastIndex
        while (endIndex > 0) {
            var current = moved[endIndex]
            if (current is File) {
                val currentLength = current.length
                var firstIndex = 0
                while (firstIndex < moved.lastIndex && (moved[firstIndex].emptyLength < currentLength)) {
                    firstIndex++
                }
                if (firstIndex < endIndex) {
                    var previous = moved[firstIndex]
                    if (previous is Split || previous.emptyLength > current.length) {
                        val newEmpty = Empty(previous.emptyLength - currentLength)
                        if (previous is Split) {
                            current = previous.copy(files = previous.files + current, newEmpty)
                        } else {
                            current = Split(listOf(current), newEmpty)
                        }
                    }
                    previous = Empty(currentLength)
                    moved[firstIndex] = current
                    moved[endIndex] = previous
                }
            }
            endIndex--
        }
        return moved
    }

    private fun calculateChecksum(blocks: List<Int>): Long =
        blocks.foldIndexed(0L) { index, sum, value ->
            if (value == EMPTY) {
                sum
            } else {
                sum + (index * value)
            }
        }

    private sealed interface BlockType {
        val length: Int
        val emptyLength: Int
        fun expand(): List<Int>
    }

    private data class File(val id: Int, override val length: Int) : BlockType {
        override val emptyLength = 0
        override fun expand() =
            (1..length).map { id }
    }

    private data class Empty(override val length: Int) : BlockType {
        override val emptyLength = length
        override fun expand() =
            (1..length).map { EMPTY }
    }

    private data class Split(
        val files: List<File>,
        val empty: Empty? = null,
    ) : BlockType {
        override val emptyLength = empty?.length ?: 0
        override val length = files.sumOf { it.length } + emptyLength
        override fun expand() =
            files.flatMap { it.expand() } + (empty?.expand() ?: emptyList())
    }

    private fun List<BlockType>.expand() =
        flatMap { it.expand() }

    private fun List<Int>.print() =
        println(map { if (it == EMPTY) '.' else it.digitToChar(36) }.joinToString(""))

    companion object : Day.Main("Day09.txt") {
        @JvmStatic
        fun main(args: Array<String>) = main()
    }
}

private const val EMPTY = -1