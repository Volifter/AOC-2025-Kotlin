package day01

import Day
import utils.toInt
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day01: Day<Int>() {
    fun getPositions(input: List<String>): Sequence<Int> =
        input.asSequence().runningFold(50) { position, line ->
            val sign = when (line.first()) {
                'L' -> -1
                'R' -> 1
                else -> throw Error("invalid direction")
            }
            position + sign * line.drop(1).toInt()
        }

    override fun solvePart1(input: List<String>): Int =
        getPositions(input).count { it.mod(100) == 0 }

    override fun solvePart2(input: List<String>): Int =
        getPositions(input).zipWithNext().sumOf { (from, to) ->
            val diff = to - from
            val dialFrom = from.mod(100)
            val dialTo = to.mod(100)
            val dialDiff = dialTo - dialFrom

            diff.absoluteValue / 100 + (
                dialFrom != 0
                && (dialTo == 0 || diff.sign != dialDiff.sign)
            ).toInt()
        }
}

fun main() = Day01().run {
    runPart(1, 3)
    runPart(2, 6)
}
