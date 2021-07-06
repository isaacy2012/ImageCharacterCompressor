package imageObjects

class Square(val dimension: Int, val pixel: Pixel) : ImageObject {
    override fun toString(): String {
        return "Square(d$dimension, $pixel)"
    }
}