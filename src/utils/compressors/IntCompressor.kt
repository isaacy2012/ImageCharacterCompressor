package utils

import CHAR_BEGIN
import CHAR_MAX
import MAX_DIMENSION
import VALUE_INT_BEGIN
import isNonSPValue
import isSPValue
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
    ret[0] = ret[0].toSP()
    return ret
}

fun Char.toSP(): Char {
    assertTrue(this.isNonSPValue())
    return this+1+MAX_DIMENSION.toInt()
}

fun Char.fromSP(): Char {
    assertTrue(this.isSPValue())
    return this-1-MAX_DIMENSION.toInt()
}

/**
 * Compresses a value into a CharArray
 */
private fun compressValue(x: Int): CharArray {
    if (x == 0) {
        return charArrayOf(encodeRawInt((0+VALUE_INT_BEGIN.toInt()).toChar()))
    }
    var dx = x;
    val ret = ArrayList<Char>()
    while (dx != 0) {
        val dxValue = dx % MAX_DIMENSION.toInt();
        ret.add(0, encodeRawInt((dxValue+VALUE_INT_BEGIN.toInt()).toChar()));
        dx /= MAX_DIMENSION.toInt();
    }
    return ret.toCharArray();
}

/**
 * Encodes an integer into character
 */
fun encodeInt(x: Int): Char {
    return encodeRawInt((x + CHAR_BEGIN.toInt()).toChar())
}

private fun encodeRawInt(x: Char): Char {
    assertTrue(x <= CHAR_MAX)
    assertTrue(x >= CHAR_BEGIN)
    return (x).toChar()
}


