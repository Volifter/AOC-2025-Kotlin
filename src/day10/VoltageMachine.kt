package day10

import java.util.PriorityQueue

class VoltageMachine(
    val voltage: List<Int>,
    val buttons: List<Button>,
    val pressed: Int = 0,
    val multiplier: Int = 1
) : IMachine, Comparable<VoltageMachine> {
    val isSolved get() = voltage.all { it == 0 }

    fun getChildren(
        combinations: Map<LightPanel, List<List<Button>>>
    ) = sequence {
        val lightPanel = LightPanel(voltage.map { it and 1 == 1 })
        val lightPanelCombinations = combinations[lightPanel]
            ?: return@sequence

        lightPanelCombinations.forEach pressedButtonsLoop@{ pressedButtons ->
            val newVoltage = voltage.toMutableList()

            pressedButtons.forEach { button ->
                button.values.forEach { i ->
                    if (--newVoltage[i] < 0)
                        return@pressedButtonsLoop
                }
            }

            yield(
                VoltageMachine(
                    newVoltage.map { it / 2 },
                    buttons,
                    pressed + pressedButtons.size * multiplier,
                    multiplier * 2
                )
            )
        }
    }

    val solutions: Sequence<VoltageMachine> get() = sequence {
        val queue = PriorityQueue<VoltageMachine>()

        queue.add(this@VoltageMachine)

        val combinations = LightMachine(LightPanel(0, voltage.size), buttons)
            .lightPanelCombinations

        while (queue.isNotEmpty()) {
            val machine = queue.poll()

            if (machine.isSolved) {
                yield(machine)
                continue
            }

            queue.addAll(machine.getChildren(combinations))
        }
    }

    override fun solve(): Int? = solutions.firstOrNull()?.pressed

    override fun compareTo(other: VoltageMachine): Int =
        pressed - other.pressed

    override fun toString(): String = (
        "VoltageMachine("
        + "voltage=$voltage, pressed=$pressed, multiplier=$multiplier"
        + ")"
    )
}
