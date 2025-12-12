package day12

import Day
import utils.Coords2D
import utils.Field
import utils.groupLines

class Day12: Day<Int>() {
    fun parseInput(
        input: List<String>
    ): List<Pair<Coords2D, Map<Field<Boolean>, Int>>> {
        val groups = groupLines(input)
        val presents = groups.dropLast(1).map { lines ->
            Field.fromLines(lines.drop(1)) { it == '#' }
        }

        return groups.last().map { line ->
            val groups = line.split(' ')
            val size = groups[0]
                .dropLast(1)
                .split('x')
                .map { it.toInt() }
                .let { (x, y) -> Coords2D(x, y) }
            val presents = groups
                .drop(1)
                .mapIndexed { i, n -> i to n.toInt() }
                .filter { it.second > 0 }
                .associate { (i, n) -> presents[i] to n }

            size to presents
        }
    }

    override fun solvePart1(input: List<String>): Int =
        parseInput(input).count { (size, presents) ->
            size.area >= presents.entries.sumOf { (present, amount) ->
                present.size.area * amount
            }
        }
}

fun main() = Day12().run {
    runPart(1, 2)
}
