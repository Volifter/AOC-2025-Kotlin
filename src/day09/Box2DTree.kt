package day09

import utils.Box2D

class Box2DTree(boxes: List<Box2D>) {
    class Node(boxes: List<Box2D>, axisIndex: Int = 0) {
        var left: Node? = null
        var right: Node? = null
        var value: Box2D? = boxes.singleOrNull()
        val bounds = Box2D.fromPoints(boxes.flatMap {
            listOf(it.start, it.end)
        })

        init {
            if (value == null) {
                val sortedBoxes = boxes.sortedBy { it.center[axisIndex] }
                val pivot = boxes.size / 2
                val nextAxisIndex = (axisIndex + 1) % 2

                left = Node(sortedBoxes.take(pivot), nextAxisIndex)
                right = Node(sortedBoxes.drop(pivot), nextAxisIndex)
            }
        }

        fun getInArea(area: Box2D): Sequence<Box2D> = sequence {
            value?.let {
                return@sequence yield(it)
            }
            left?.let {
                if (!area.intersect(it.bounds).isEmpty)
                    yieldAll(it.getInArea(area))
            }
            right?.let {
                if (!area.intersect(it.bounds).isEmpty)
                    yieldAll(it.getInArea(area))
            }
        }
    }

    val root = Node(boxes)

    fun getInArea(area: Box2D): Sequence<Box2D> = root.getInArea(area)
}
