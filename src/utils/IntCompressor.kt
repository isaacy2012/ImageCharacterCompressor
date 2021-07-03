package utils

import CHAR_BEGIN
import CHAR_MAX
import MAX_DIMENSION
import VALUE_INT_BEGIN
import kotlin.test.assertTrue


/**
 * Compresses a dimension [that comes after a double pixel]
 * into a CharArray
 */
fun Int.compressDimension(): CharArray {
    return compressValue(this)
}

/**
 * Compresses a dimension that comes after a single pixel
 * into a CharArray
 */
fun Int.compressDimensionSP(): CharArray {
    val ret = compressValue(this)
    ret[0] = ret[0] + 1 + MAX_DIMENSION
    return ret
}

/**
 * Compresses a value into a CharArray
 */
private fun compressValue(x: Int): CharArray {
        var dx = x;
        val ret = ArrayList<Char>()
        while (dx != 0) {
            val dxValue = dx % MAX_DIMENSION;
            ret.add(0, encodeRawInt(dxValue+VALUE_INT_BEGIN));
            dx /= MAX_DIMENSION;
        }
        return ret.toCharArray();
}

/**
 * Encodes an integer into character
 */
fun encodeInt(x: Int): Char {
    return encodeRawInt(x + CHAR_BEGIN)
}

fun encodeRawInt(x: Int): Char {
    assertTrue(x <= CHAR_MAX)
    assertTrue(x >= CHAR_BEGIN)
    return (x).toChar()
}


