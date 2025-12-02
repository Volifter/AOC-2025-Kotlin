package day02

import Day

class Day02: Day<Long>() {
    fun parseInput(input: List<String>): List<LongRange> =
        input.single().split(",").map { range ->
            range.split("-").map { it.toLong() }.let { (a, b) -> a..b }
        }

    fun getRangeStart(size: Int): Long = ("1" + "0".repeat(size - 1)).toLong()

    fun getRangeEnd(size: Int): Long = "9".repeat(size).toLong()

    fun getEqualSizeRanges(fullRange: LongRange) = sequence {
        var range = (
            fullRange.first..getRangeEnd(fullRange.first.toString().length)
        )

        while (range.last < fullRange.last) {
            yield(range)

            val nextLength = range.first.toString().length + 1

            range = getRangeStart(nextLength)..getRangeEnd(nextLength)
        }

        yield(range.first..fullRange.last)
    }

    fun getInvalidIdsInRange(
        range: LongRange,
        maxGroupCount: Int = 2
    ): Set<Long> = buildSet {
        getEqualSizeRanges(range).forEach { range ->
            (2..maxGroupCount).forEach { groupCount ->
                val groupSize = range.first.toString().length / groupCount

                if (groupSize * groupCount != range.first.toString().length)
                    return@forEach

                val start = range.first.toString().take(groupSize).toLong()

                addAll(
                    generateSequence(start) { it + 1 }
                        .map { it.toString().repeat(groupCount).toLong() }
                        .dropWhile { it < range.first }
                        .takeWhile { it <= range.last }
                )
            }
        }
    }

    override fun solvePart1(input: List<String>): Long =
        parseInput(input).sumOf { range -> getInvalidIdsInRange(range).sum() }

    override fun solvePart2(input: List<String>): Long =
        parseInput(input).sumOf { range ->
            getInvalidIdsInRange(range, range.last.toString().length).sum()
        }
}

fun main() = Day02().run {
    runPart(1, 1227775554)
    runPart(2, 4174379265)
}
