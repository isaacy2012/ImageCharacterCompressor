package imageObjects

import PIXEL_MAX_DEPTH
import SINGLE_PIXEL_CHAR
import utils.encodeInt

open class Pixel(val color: Int): ImageObject {
    init {
        assert(color >= 0)
        assert(color <= PIXEL_MAX_DEPTH)
    }

    fun compressWithNext(next: Pixel): Char {
        return encodeInt((this.color shl 3) or next.color)
    }

    fun compressSingle(): CharArray {
        return charArrayOf(encodeInt(this.color shl 3), SINGLE_PIXEL_CHAR)
    }
}
