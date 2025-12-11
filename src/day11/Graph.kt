package day11

class Graph(val nodes: MutableMap<String, Node>) {
    fun pruneAncestors(root: Node) {
        var stack = nodes.values.filter {
            it != root && it.parents.isEmpty()
        }

        while (stack.isNotEmpty()) {
            stack = buildList {
                stack.forEach { node ->
                    nodes.remove(node.name)

                    node.children.forEach { child ->
                        child.parents.remove(node)

                        if (child != root && child.parents.isEmpty())
                            add(child)
                    }
                }
            }
        }
    }

    fun countPaths(
        startNode: Node,
        endNode: Node,
        intermediateStops: Set<Node> = setOf()
    ): Long {
        val counts: MutableMap<Node, Long> = mutableMapOf(startNode to 1)
        val remainingStops = intermediateStops.toMutableSet()

        pruneAncestors(startNode)

        startNode.topologicalDescendants.forEach { node ->
            val count = counts[node] ?: 0

            if (node == endNode) {
                if (remainingStops.isNotEmpty())
                    throw Error("No path through all intermediary stops")

                return count
            }

            if (remainingStops.remove(node)) {
                counts.clear()
                counts[node] = count
            }

            node.children.forEach { child ->
                counts[child] = count + (counts[child] ?: 0)
            }
        }

        throw Error("$endNode is unreachable")
    }

    operator fun get(name: String) = nodes[name]
}
