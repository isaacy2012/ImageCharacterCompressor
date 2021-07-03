package utils

import CHAR_BEGIN
import INT_RAW_MAX
import MAX_DIMENSION_INT
import imageObjects.Pixel
import kotlin.test.assertTrue

fun compressInt(x: Int): CharArray {
        // take the given number
        // convert it into decimal
        // divide the decimal with the target base
        var dx = x;

        // must be in decimal
        val ret = ArrayList<Char>()
        while (dx != 0) {
            val dxValue = dx % MAX_DIMENSION_INT;
            ret.add(0, encodeRawInt(dxValue));
            dx /= MAX_DIMENSION_INT;
        }
        return ret.toCharArray();
}

fun encodeRawInt(x: Int): Char {
    assertTrue(x <= INT_RAW_MAX-CHAR_BEGIN)
    assertTrue(x >= 0)
    return (CHAR_BEGIN+x).toChar()
}

fun encodePixelPair(a: Pixel, b: Pixel): Char {
    return encodeRawInt((a.color shl 3) or b.color)
}

