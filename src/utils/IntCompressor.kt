package utils

import CHAR_BEGIN
import CHAR_MAX
import MAX_DIMENSION
import VALUE_INT_BEGIN
import kotlin.test.assertTrue


fun Int.compressDimension(): CharArray {
    return compressInt(this)
}

fun Int.compressDimensionSP(): CharArray {
    val ret = compressInt(this)
    ret[0] = ret[0] + 1 + MAX_DIMENSION
    return ret
}

private fun compressInt(x: Int): CharArray {
        var dx = x;
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


