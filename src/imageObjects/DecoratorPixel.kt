package imageObjects

class DecoratorPixel(x: Int, y: Int, color: Int) : EncodableImageObject {
    val pixel: Pixel = Pixel(color)
    val point: Point = Point(x, y)

    override fun encode(): CharArray {
        TODO("Not yet implemented")
    }
}