package day07

import Day
import utils.Coords2D
import utils.Field

class Day07: Day<Long>() {
    fun parseInput(input: List<String>): Pair<Field<Char>, Coords2D> =
        Field.fromLines(input) { it }.let { field ->
            field to field.find { field[it] == 'S' }!!
        }

    override fun solvePart1(input: List<String>): Long {
        val (field, startCoords) = parseInput(input)

        return (startCoords.y + 1..<field.size.y)
            .fold(setOf(startCoords.x) to 0L) { (beams, prevSplits), y ->
                var splits = prevSplits

                buildSet {
                    beams.forEach { x ->
                        if (field[Coords2D(x, y)] == '^') {
                            add(x - 1)
                            add(x + 1)
                            splits++
                        } else
                            add(x)
                    }
                } to splits
            }.second
    }

    override fun solvePart2(input: List<String>): Long {
        val (field, startCoords) = parseInput(input)

        return (startCoords.y + 1..<field.size.y)
            .fold(mapOf(startCoords.x to 1L)) { beams, y ->
                buildMap {
                    fun add(x: Int, count: Long) =
                        set(x, getOrDefault(x, 0) + count)

                    beams.forEach { (x, count) ->
                        if (field[Coords2D(x, y)] == '^') {
                            add(x - 1, count)
                            add(x + 1, count)
                        } else
                            add(x, count)
                    }
                }
            }.values.sum()
    }
}

fun main() = Day07().run {
    runPart(1, 21)
    runPart(2, 40)
}
