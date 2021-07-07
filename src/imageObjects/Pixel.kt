package imageObjects

import CHAR_BEGIN
import PIXEL_MAX_DEPTH
import utils.encodeInt

fun getPixelPairFromChar(ch: Char): Array<Pixel> {
    // Pixel pair
    val left = (((ch - CHAR_BEGIN) ushr 3) or 0)
    val right = ((ch - CHAR_BEGIN) and PIXEL_MAX_DEPTH)
    return arrayOf(Pixel(left), Pixel(right))
}

data class Pixel(val color: Int): ImageObject {
    init {
        assert(color >= 0) {"color $color was below 0"}
        assert(color <= PIXEL_MAX_DEPTH) {"color $color was too high"}
    }

    /**
     * Compress with the next pixel by bit-shifting this pixel 3 left, and then
     * adding the next pixel to the lower 3 bits:
     * aaabbb
     * @param next the next pixel
     * @return the encoded Char
     */
    fun compressWithNext(next: Pixel): Char {
        return encodeInt((this.color shl 3) or next.color)
    }

    /**
     * Compress this pixel by itself. This wastes the space (3 bits) for a second pixel,
     * and relies on the next token to signify this encoded pixel pair
     * is actually only one pixel:
     * aaa000
     * @return the encoded Char
     */
    fun compressSingle(): Char {
        return encodeInt(this.color shl 3)
    }

    override fun toString(): String {
        return "$color"
    }
}
