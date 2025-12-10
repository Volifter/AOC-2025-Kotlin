package day10

class LightPanel(val value: Int, val size: Int) {
    constructor(values: List<Boolean>) : this(
        values.reversed().fold(0) { acc, isOn ->
            acc shl 1 or if (isOn) 1 else 0
        },
        values.size
    )

    val isSolved = value == 0

    fun getSwitched(button: Button) = LightPanel(
        value xor button.values.fold(0) { acc, index -> acc or (1 shl index) },
        size
    )

    override fun hashCode(): Int = value.hashCode()

    override fun equals(other: Any?): Boolean = other is LightPanel
        && other.value == value

    override fun toString(): String = value.toString(2).padStart(size, '0')
}
