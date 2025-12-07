package utils

data class Field<T>(val data: List<List<T>>) {
    operator fun get(coords: Coords2D) = coords.run { data[y][x] }

    val size: Coords2D = Coords2D(data.first().size, data.size)

    fun map(callback: (Coords2D) -> T) =
        buildField(size, callback)

    fun find(callback: (Coords2D) -> Boolean) =
        Coords2D.iterate(size).find(callback)

    fun getNeighbors(coords: Coords2D): Sequence<Coords2D> =
        coords.neighbors.filter { it in size }

    companion object {
        fun <T> buildField(size: Coords2D, callback: (Coords2D) -> T) =
            Field(
                (0..<size.y).map { y ->
                    (0..<size.x).map { x ->
                        callback(Coords2D(x, y))
                    }
                }
            )

        fun <T> fromLines(lines: List<String>, callback: (Char) -> T) =
            buildField(Coords2D(lines.first().length, lines.size)) { (x, y) ->
                callback(lines[y][x])
            }
    }
}
