package day08

import utils.Box3D
import utils.Coords3D
import java.util.PriorityQueue
import kotlin.math.sign

class Coords3DTree(points: List<Coords3D>) {
    data class Node(val points: List<Coords3D>, val axisIndex: Int = 0) {
        var left: Node? = null
        var right: Node? = null
        var value: Coords3D? = null
        val bounds: Box3D = Box3D.fromPoints(points)

        val isLeaf: Boolean get() = left == null && right == null

        init {
            if (points.size == 1) {
                value = points.single()
            } else {
                val sortedCoords = points.sortedBy { it[axisIndex] }
                val pivot = sortedCoords.size / 2
                val nextAxis = (axisIndex + 1) % 3

                value = sortedCoords[pivot]
                left = Node(sortedCoords.take(pivot), nextAxis)
                right = Node(sortedCoords.drop(pivot), nextAxis)
            }
        }
    }

    data class NodePair(
        val distance: Float,
        val nodeA: Node,
        val nodeB: Node
    ) : Comparable<NodePair> {
        override fun compareTo(other: NodePair): Int =
            (distance - other.distance).sign.toInt()

        companion object {
            fun fromNodes(nodeA: Node, nodeB: Node) = if (nodeA === nodeB)
                NodePair(0f, nodeA, nodeA)
            else
                NodePair(
                    nodeA.bounds.getMinDistanceTo(nodeB.bounds),
                    nodeA,
                    nodeB
                )
        }
    }

    val root = Node(points)

    val closestPairs: Sequence<Pair<Coords3D, Coords3D>> get() = sequence {
        val queue = PriorityQueue<NodePair>()

        queue.add(NodePair(0f, root, root))

        while (queue.isNotEmpty()) {
            val (_, nodeA, nodeB) = queue.poll()

            when {
                nodeA.isLeaf && nodeB.isLeaf -> {
                    if (nodeA !== nodeB)
                        yield(nodeA.value!! to nodeB.value!!)
                }
                nodeA === nodeB -> {
                    val left = nodeA.left!!
                    val right = nodeA.right!!

                    queue.add(NodePair.fromNodes(left, left))
                    queue.add(NodePair.fromNodes(right, right))
                    queue.add(NodePair.fromNodes(left, right))
                }
                else -> {
                    val (splitNode, otherNode) =
                        if (
                            nodeB.isLeaf
                            || nodeA.bounds.delta > nodeB.bounds.delta
                        )
                            nodeA to nodeB
                        else
                            nodeB to nodeA

                    val left = splitNode.left!!
                    val right = splitNode.right!!

                    queue.add(NodePair.fromNodes(otherNode, left))
                    queue.add(NodePair.fromNodes(otherNode, right))
                }
            }
        }
    }
}
