package imageObjects

import Compressible
import PIXEL_MAX_DEPTH
import utils.encodePixelPair
import utils.encodeRawInt

open class Pixel(val color: Int): ImageObject {
    init {
        assert(color >= 0)
        assert(color <= PIXEL_MAX_DEPTH)
    }

    fun compressWithNext(next: Pixel): Char {
        return encodePixelPair(this, next)
    }
}
