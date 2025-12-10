package day10

import java.util.PriorityQueue

class LightMachine(
    val lights: LightPanel,
    val buttons: List<Button>,
    val pressedButtons: List<Button> = listOf()
) : IMachine, Comparable<LightMachine> {
    val isSolved get() = lights.isSolved

    val children: Sequence<LightMachine> get() = sequence {
        val queue = PriorityQueue<LightMachine>()

        queue.add(this@LightMachine)

        while (queue.isNotEmpty()) {
            val machine = queue.poll()

            if (machine.buttons.isEmpty()) {
                yield(machine)
                continue
            }

            val button = machine.buttons.first()
            val newButtons = machine.buttons.drop(1)

            queue.add(
                LightMachine(
                    machine.lights.getSwitched(button),
                    newButtons,
                    machine.pressedButtons + button
                )
            )
            queue.add(
                LightMachine(
                    machine.lights,
                    newButtons,
                    machine.pressedButtons
                )
            )
        }
    }

    val lightPanelCombinations get() = buildMap {
        children
            .forEach { lightMachine ->
                val key = lightMachine.lights

                this[key] = (
                    getOrDefault(key, listOf())
                        + listOf(lightMachine.pressedButtons)
                    )
            }
    }

    override fun solve(): Int? = children
        .firstOrNull { it.isSolved }
        ?.pressedButtons
        ?.size

    override fun compareTo(other: LightMachine): Int =
        pressedButtons.size - other.pressedButtons.size

    override fun toString(): String =
        "LightMachine(lights=$lights, pressedButtons=$pressedButtons)"
}
