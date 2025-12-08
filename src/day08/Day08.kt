package day08

import Day
import utils.Coords3D
import utils.prod

class Day08: Day<Long>() {
    fun parseInput(input: List<String>): List<Coords3D> =
        input.map { line ->
            line
                .split(",")
                .map { it.toInt() }
                .let { (x, y, z) -> Coords3D(x, y, z) }
        }

    override fun solvePart1(input: List<String>): Long {
        val boxes = parseInput(input)
        val groups = mutableMapOf<Coords3D, MutableSet<Coords3D>>()
        val count = if (input.size == 20) 10 else 1000
        val pairs = Coords3DTree(boxes).closestPairs.take(count)

        pairs.forEach { (boxA, boxB) ->
            val groupA = groups.getOrPut(boxA) { mutableSetOf(boxA) }
            val groupB = groups.getOrPut(boxB) { mutableSetOf(boxB) }

            if (groupA !== groupB) {
                groupA.addAll(groupB)
                groupB.forEach { coords -> groups[coords] = groupA }
            }
        }

        return groups.values
            .toSet()
            .map { it.size }
            .sortedDescending()
            .take(3)
            .prod()
    }

    override fun solvePart2(input: List<String>): Long {
        val boxes = parseInput(input)
        val pairs = Coords3DTree(boxes).closestPairs
        val remaining = boxes.toMutableSet()

        pairs.forEach { (boxA, boxB) ->
            remaining.remove(boxA)
            remaining.remove(boxB)

            if (remaining.isEmpty())
                return 1L * boxA.x * boxB.x
        }

        throw Error("Boxes' connections don't form one graph")
    }
}

fun main() = Day08().run {
    runPart(1, 40)
    runPart(2, 25272)
}
