package imageObjects

class DecoratorSquare(x: Int, y: Int, dimension: Int, color: Int) : CompressibleImageObject {
    val pixel: Square = Square(dimension, Pixel(color))
    val point: Point = Point(x, y)

    override fun compress(): CharArray {
        TODO("Not yet implemented")
    }
}