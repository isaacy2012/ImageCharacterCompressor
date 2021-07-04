
const val MAX_PIXELS = 900
const val PIXEL_MAX_DEPTH = 7
const val RGB_MAX_DEPTH = 255

const val K = PIXEL_MAX_DEPTH / RGB_MAX_DEPTH.toDouble()

const val DECORATOR_START_ID = 255.toChar()
const val DECORATOR_INT_ID = 254.toChar()
const val DECORATOR_END_ID = 253.toChar()
const val LAST = 251.toChar()

const val RED_WEIGHT = 0.3
const val GREEN_WEIGHT = 0.59
const val BLUE_WEIGHT = 0.11

const val CHAR_MAX:Char = 255.toChar()
const val CHAR_BEGIN: Char = 32.toChar()
const val PIXEL_INT_RANGE: Char = 64.toChar()
const val VALUE_INT_BEGIN: Char = CHAR_BEGIN + PIXEL_INT_RANGE.toInt() + 1
const val MAX_DIMENSION: Char = ((LAST.toInt()-VALUE_INT_BEGIN.toInt()-2)/2).toChar() //-2 for 2 zero-values
const val VALUE_INT_BEGIN_SP: Char = (CHAR_BEGIN+PIXEL_INT_RANGE.toInt()+1+MAX_DIMENSION.toInt()+1)
const val INT_RAW_MAX: Char = (CHAR_MAX.toInt()-CHAR_BEGIN.toInt()).toChar()

fun Char.isPixel(): Boolean {
    return this >= CHAR_BEGIN && this < VALUE_INT_BEGIN
}

fun Char.isOpCode(): Boolean {
    return this >= LAST && this <=  255.toChar()
}

fun Char.isValue(): Boolean {
    return this in CHAR_BEGIN until LAST
}

fun Char.isSPValue(): Boolean {
    return this in VALUE_INT_BEGIN_SP until LAST
}

fun Char.isNonSPValue(): Boolean {
    return this in VALUE_INT_BEGIN until VALUE_INT_BEGIN_SP
}


