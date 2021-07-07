package imageObjects

data class Square(var dimension: Int, val pixel: Pixel) : ImageObject, Comparable<Square> {
    override fun toString(): String {
        return "Square(d$dimension, $pixel)"
    }

    override fun compareTo(other: Square): Int {
        return this.dimension.compareTo(other.dimension)
    }
}