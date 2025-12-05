package day05

import Day
import utils.groupLines

class Day05: Day<Long>() {
    fun parseInput(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val (ranges, values) = groupLines(input)

        return ranges.map { line ->
            line
                .split("-")
                .map { it.toLong() }
                .let { (first, last) -> first..last }
        } to values.map { it.toLong() }
    }

    override fun solvePart1(input: List<String>): Long {
        val (ranges, values) = parseInput(input)

        return values.count { n -> ranges.any { n in it } }.toLong()
    }

    fun mergeRanges(ranges: Set<LongRange>): Set<LongRange>? {
        ranges.forEachIndexed { i, rangeA ->
            ranges.asSequence().drop(i + 1).forEach { rangeB ->
                if (
                    rangeA.first in rangeB
                    || rangeA.last in rangeB
                    || rangeB.first in rangeA
                    || rangeB.last in rangeA
                    || rangeA.last + 1 == rangeB.first
                    || rangeB.last + 1 == rangeA.first
                ) {
                    return (
                        ranges
                            - setOf(rangeA, rangeB)
                            + setOf(
                                minOf(rangeA.first, rangeB.first)
                                    ..maxOf(rangeA.last, rangeB.last)
                            )
                    )
                }
            }
        }

        return null
    }

    override fun solvePart2(input: List<String>): Long {
        val ranges = generateSequence(parseInput(input).first.toSet()) {
            mergeRanges(it)
        }.last()

        return ranges.sumOf { it.last - it.first + 1 }
    }
}

fun main() = Day05().run {
    runPart(1, 3)
    runPart(2, 14)
}
