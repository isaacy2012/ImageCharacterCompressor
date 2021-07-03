package imageObjects

import Compressible

class DecoratorPixel(x: Int, y: Int, color: Int) : CompressibleImageObject {
    val pixel: Pixel = Pixel(color)
    val point: Point = Point(x, y)

    override fun compress(): CharArray {
        TODO("Not yet implemented")
    }
}