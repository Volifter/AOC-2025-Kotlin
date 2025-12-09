package day09

import Day
import utils.Box2D
import utils.Coords2D

class Day09: Day<Long>() {
    fun parseInput(input: List<String>): List<Coords2D> =
        input.map { line ->
            line.split(",").map { it.toInt() }.let { (x, y) -> Coords2D(x, y) }
        }.let { list ->
            val min = list.min()
            val i = list.indexOf(min)

            list.drop(i) + list.take(i)
        }

    fun getRectangles(corners: List<Coords2D>): Sequence<Box2D> =
        sequence {
            corners.forEachIndexed { i, cornerA ->
                corners.drop(i + 1).forEach { cornerB ->
                    yield(Box2D.fromCorners(cornerA, cornerB))
                }
            }
        }

    override fun solvePart1(input: List<String>): Long =
        getRectangles(parseInput(input)).maxOf { it.area }

    override fun solvePart2(input: List<String>): Long {
        val corners = parseInput(input)
        val rectangles = getRectangles(corners)
        val pairs = (corners + listOf(corners.first()))
            .zipWithNext()
            .toSet()
        val edges = pairs.map { (from, to) -> Box2D.fromCorners(from, to) }
        val directions = edges.associateWith { (x, y) -> (x to y) in pairs }
        val clockwiseEdges = edges.first().run { start.y == end.y }
        val tree = Box2DTree(edges)

        return rectangles.maxOf { box ->
            val topRightCorner = Coords2D(box.end.x, box.start.y)
            val bottomLeftCorner = Coords2D(box.start.x, box.end.y)

            if (
                listOf(
                    Box2D(box.start, topRightCorner) to clockwiseEdges,
                    Box2D(topRightCorner, box.end) to clockwiseEdges,
                    Box2D(bottomLeftCorner, box.end) to !clockwiseEdges,
                    Box2D(box.start, bottomLeftCorner) to !clockwiseEdges
                ).all { (box, isClockwise) ->
                    directions[box]?.let { it == isClockwise } ?: true
                }
                && tree.getInArea(
                    Box2D(
                        box.start + Coords2D(1, 1),
                        box.end - Coords2D(1, 1)
                    )
                ).firstOrNull() == null
            )
                box.area
            else
                0L
        }
    }
}

fun main() = Day09().run {
    runPart(1, 50)
    runPart(2, 24)
}
