package utils

import CHAR_BEGIN
import CHAR_MAX
import MAX_DIMENSION
import VALUE_INT_BEGIN
import kotlin.test.assertTrue


fun Int.compressDimension(): CharArray {
    return compressInt(this)
}

private fun compressInt(x: Int): CharArray {
        // take the given number
        // convert it into decimal
        // divide the decimal with the target base
        var dx = x;

        // must be in decimal
        val ret = ArrayList<Char>()
        while (dx != 0) {
            val dxValue = dx % MAX_DIMENSION;
            ret.add(0, encodeRawInt(dxValue+VALUE_INT_BEGIN));
            dx /= MAX_DIMENSION;
        }
        return ret.toCharArray();
}

fun encodeInt(x: Int): Char {
    return encodeRawInt(x + CHAR_BEGIN)
}

fun encodeRawInt(x: Int): Char {
    assertTrue(x <= CHAR_MAX)
    assertTrue(x >= CHAR_BEGIN)
    return (x).toChar()
}


