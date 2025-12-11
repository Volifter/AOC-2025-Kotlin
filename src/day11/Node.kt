package day11

class Node(val name: String) {
    val children: MutableSet<Node> = mutableSetOf()
    val parents: MutableSet<Node> = mutableSetOf()

    val topologicalDescendants get() = sequence {
        var stack = listOf(this@Node)
        val visitedEdges: MutableMap<Node, MutableSet<Node>> = mutableMapOf()

        while (stack.isNotEmpty()) {
            stack = buildList {
                stack.forEach { node ->
                    yield(node)

                    node.children.forEach { child ->
                        val visitedParents = visitedEdges.getOrPut(child) {
                            mutableSetOf()
                        }

                        visitedParents.add(node)

                        if (child.parents.size == visitedParents.size)
                            add(child)
                    }
                }
            }
        }
    }

    override fun toString(): String =
        "Node($name -> ${children.joinToString(", ") { it.name }})"
}
