package imageObjects

class PointSquare(x: Int, y: Int, dimension: Int, color: Int) : EncodableImageObject, Comparable<PointSquare> {
    val square: Square = Square(dimension, Pixel(color))
    val point: Point = Point(x, y)

    constructor(ps: PointSquare, newDimension: Int) : this(ps.point.x, ps.point.y, newDimension, ps.square.pixel.color)

    override fun encode(): CharArray {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: PointSquare): Int {
        return this.square.compareTo(other.square)
    }
}