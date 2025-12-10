package day10

class Button(val values: Set<Int>) {
    val size: Int get() = values.size

    operator fun contains(value: Int) = value in values

    override fun toString(): String = "(" + values.joinToString(",") + ")"
}
