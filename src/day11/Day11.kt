package day11

import Day

class Day11: Day<Long?>() {
    fun parseInput(input: List<String>): Graph =
        Graph(buildMap {
            input.forEach { line ->
                val parts = line.split(' ')
                val name = parts[0].dropLast(1)
                val node = getOrPut(name) { Node(name) }
                val children = parts.drop(1).map { name ->
                    getOrPut(name) { Node(name) }
                        .also { it.parents += setOf(node) }
                }

                node.children += children
            }
        }.toMutableMap())

    override fun solvePart1(input: List<String>): Long? {
        val graph = parseInput(input)

        return graph.countPaths(
            graph["you"] ?: return null,
            graph["out"]!!
        )
    }

    override fun solvePart2(input: List<String>): Long? {
        val graph = parseInput(input)

        return graph.countPaths(
            graph["svr"] ?: return null,
            graph["out"]!!,
            setOf(graph["fft"]!!, graph["dac"]!!)
        )
    }
}

fun main() = Day11().run {
    runPart(1, 5, null)
    runPart(2, null, 2)
}
