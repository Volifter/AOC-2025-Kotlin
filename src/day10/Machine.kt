package day10

class Machine(
    val lightPanel: LightPanel,
    val buttons: List<Button>,
    val voltage: List<Int>
) {
    fun toLightMachine() =
        LightMachine(lightPanel, buttons)

    fun toVoltageMachine() =
        VoltageMachine(voltage, buttons)
}
