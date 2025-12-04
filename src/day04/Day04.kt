package day04

import Day
import utils.Field

class Day04: Day<Int>() {
    fun mutateField(field: Field<Boolean>): Pair<Field<Boolean>, Int> {
        var count = 0
        val newField = field.map { coords ->
            field[coords]
            && (field.getNeighbors(coords).count { field[it] } > 3)
                .also { if (!it) count++ }
        }

        return newField to count
    }

    fun getFieldMutationCounts(input: List<String>): Sequence<Int> =
        generateSequence(
            Field.fromLines(input) { it == '@' } to 0
        ) { (field, count) ->
            val (newField, newCount) = mutateField(field)

            (newField to count + newCount).takeIf { newCount > 0 }
        }
            .drop(1)
            .map { it.second }

    override fun solvePart1(input: List<String>): Int =
        getFieldMutationCounts(input).first()

    override fun solvePart2(input: List<String>): Int =
        getFieldMutationCounts(input).last()
}

fun main() = Day04().run {
    runPart(1, 13)
    runPart(2, 43)
}
