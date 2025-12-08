package utils

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

data class Box3D(val start: Coords3D, val end: Coords3D) {
    val isEmpty = end.x < start.x || end.y < start.y || end.z < start.z

    val isNotEmpty = !isEmpty

    val xRange get() = (start.x..end.x)

    val yRange get() = (start.y..end.y)

    val zRange get() = (start.z..end.z)

    val delta get() = sqrt(
        ((end.x - start.x) * 1f).pow(2f)
        + ((end.y - start.y) * 1f).pow(2f)
        + ((end.z - start.z) * 1f).pow(2f)
    )

    val manhattanDelta: Int get() = (
        (end.x - start.x).absoluteValue
        + (end.y - start.y).absoluteValue
        + (end.z - start.z).absoluteValue
    )

    fun intersect(other: Box3D): Box3D =
        Box3D(
            Coords3D(
                maxOf(start.x, other.start.x),
                maxOf(start.y, other.start.y),
                maxOf(start.z, other.start.z)
            ),
            Coords3D(
                minOf(end.x, other.end.x),
                minOf(end.y, other.end.y),
                minOf(end.z, other.end.z)
            )
        )

    fun getMinDistanceTo(other: Box3D) = sqrt((0..2).fold(0f) { acc, i ->
        val (left, right) = if (start[i] < other.start[i])
            this to other
        else
            other to this

        acc + maxOf(0f, (right.start[i] - left.end[i]) * 1f).pow(2)
    })

    operator fun plus(offset: Coords3D) =
        Box3D(start + offset, end + offset)

    operator fun minus(offset: Coords3D) =
        Box3D(start - offset, end - offset)

    companion object {
        fun fromCorners(cornerA: Coords3D, cornerB: Coords3D): Box3D =
            Box3D(
                Coords3D(
                    minOf(cornerA.x, cornerB.x),
                    minOf(cornerA.y, cornerB.y),
                    minOf(cornerA.z, cornerB.z)
                ),
                Coords3D(
                    maxOf(cornerA.x, cornerB.x),
                    maxOf(cornerA.y, cornerB.y),
                    maxOf(cornerA.z, cornerB.z)
                )
            )

        fun fromPoints(points: List<Coords3D>): Box3D =
            Box3D(
                Coords3D(
                    points.minOf { it.x },
                    points.minOf { it.y },
                    points.minOf { it.z }
                ),
                Coords3D(
                    points.maxOf { it.x },
                    points.maxOf { it.y },
                    points.maxOf { it.z }
                )
            )
    }
}
