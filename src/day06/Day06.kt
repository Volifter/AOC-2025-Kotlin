package day06

import Day
import utils.groupLines
import utils.rotateLinesCCW

class Day06: Day<Long>() {
    fun solve(problems: List<Pair<List<Long>, Char>>): Long =
        problems.sumOf { (numbers, operator) ->
            when (operator) {
                '+' -> numbers.sum()
                '*' -> numbers.reduce { a, b -> a * b }
                else -> throw Error("Invalid operator: '$operator'")
            }
        }

    override fun solvePart1(input: List<String>): Long {
        val rows = input.map { line ->
            line.split(" ").filter { it.isNotEmpty() }
        }

        return solve(rows.first().indices.map { i ->
            (
                rows.dropLast(1).map { row -> row[i].toLong() }
            ) to rows.last()[i].single()
        })
    }

    override fun solvePart2(input: List<String>): Long {
        val maxLength = input.maxOf { it.length }
        val padded = input.map { it.padEnd(maxLength, ' ') }
        val groups = groupLines(rotateLinesCCW(padded).map { it.trim() })

        return solve(groups.map { lines ->
            (
                listOf(lines.last().dropLast(1).trim().toLong())
                + lines.dropLast(1).map { it.toLong() }
            ) to lines.last().last()
        })
    }
}

fun main() = Day06().run {
    runPart(1, 4277556)
    runPart(2, 3263827)
}
