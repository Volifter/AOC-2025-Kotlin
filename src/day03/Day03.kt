package day03

import Day

class Day03: Day<Long>() {
    fun joinValues(values: List<Int>): Long =
        values.fold(0L) { acc, n -> acc * 10 + n }

    fun prependValue(value: Int, values: Long): Long =
        if (values == 0L) value.toLong() else "$value$values".toLong()

    fun solveLine(values: List<Int>, size: Int): Long =
        when (size) {
            0 -> 0L
            values.size -> joinValues(values)
            else -> {
                val count = values.size - size + 1
                val max = values.take(count).max()
                val i = values.indexOf(max)

                prependValue(max, solveLine(values.drop(i + 1), size - 1))
            }
        }

    fun solve(input: List<String>, size: Int) =
        input.sumOf { line -> solveLine(line.map { it.digitToInt() }, size) }

    override fun solvePart1(input: List<String>): Long = solve(input, 2)

    override fun solvePart2(input: List<String>): Long = solve(input, 12)
}

fun main() = Day03().run {
    runPart(1, 357)
    runPart(2, 3121910778619)
}
