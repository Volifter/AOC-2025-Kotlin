package utils

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

data class Box2D(val start: Coords2D, val end: Coords2D) {
    val isEmpty = end.x < start.x || end.y < start.y

    val isNotEmpty = !isEmpty

    val xRange get() = (start.x..end.x)

    val yRange get() = (start.y..end.y)

    val delta get() = sqrt(
        ((end.x - start.x) * 1f).pow(2f)
        + ((end.y - start.y) * 1f).pow(2f)
    )

    val manhattanDelta: Int get() = (
        (end.x - start.x).absoluteValue
        + (end.y - start.y).absoluteValue
    )

    val area get() = (1L + end.x - start.x) * (1L + end.y - start.y)

    val center get() = Coords2D(
        (end.x - start.x) / 2,
        (end.y - start.y) / 2
    )

    fun intersect(other: Box2D): Box2D =
        Box2D(
            Coords2D(
                maxOf(start.x, other.start.x),
                maxOf(start.y, other.start.y)
            ),
            Coords2D(
                minOf(end.x, other.end.x),
                minOf(end.y, other.end.y)
            )
        )

    fun getMinDistanceTo(other: Box2D) = sqrt((0..2).fold(0f) { acc, i ->
        val (left, right) = if (start[i] < other.start[i])
            this to other
        else
            other to this

        acc + maxOf(0f, (right.start[i] - left.end[i]) * 1f).pow(2)
    })

    operator fun plus(offset: Coords2D) =
        Box2D(start + offset, end + offset)

    operator fun minus(offset: Coords2D) =
        Box2D(start - offset, end - offset)

    companion object {
        fun fromCorners(cornerA: Coords2D, cornerB: Coords2D): Box2D =
            Box2D(
                Coords2D(
                    minOf(cornerA.x, cornerB.x),
                    minOf(cornerA.y, cornerB.y)
                ),
                Coords2D(
                    maxOf(cornerA.x, cornerB.x),
                    maxOf(cornerA.y, cornerB.y)
                )
            )

        fun fromPoints(points: List<Coords2D>): Box2D =
            Box2D(
                Coords2D(
                    points.minOf { it.x },
                    points.minOf { it.y }
                ),
                Coords2D(
                    points.maxOf { it.x },
                    points.maxOf { it.y }
                )
            )
    }
}
