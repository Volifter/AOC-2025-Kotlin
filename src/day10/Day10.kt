package day10

import Day

class Day10: Day<Int>() {
    fun parseInput(input: List<String>): List<Machine> = input.map { line ->
        val items = line.split(' ')

        Machine(
            LightPanel(items.first().drop(1).dropLast(1).map { it == '#' }),
            items.drop(1).dropLast(1).map { buttons ->
                Button(
                    buttons.drop(1).dropLast(1).split(',')
                        .map { it.toInt() }
                        .toSet()
                )
            },
            items.last().drop(1).dropLast(1).split(',').map { it.toInt() }
        )
    }

    fun solve(machines: List<IMachine>): Int =
        machines.sumOf { machine ->
            machine.solve()
                ?: throw Error("$machine has no solutions")
        }

    override fun solvePart1(input: List<String>): Int =
        solve(parseInput(input).map { it.toLightMachine() })

    override fun solvePart2(input: List<String>): Int =
        solve(parseInput(input).map { it.toVoltageMachine() })
}

fun main() = Day10().run {
    runPart(1, 7)
    runPart(2, 33)
}
