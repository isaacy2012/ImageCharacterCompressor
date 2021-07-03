
const val MAX_PIXELS = 900
const val PIXEL_MAX_DEPTH = 7
const val RGB_MAX_DEPTH = 255

const val K = PIXEL_MAX_DEPTH / RGB_MAX_DEPTH.toDouble()

const val DECORATOR_START_ID = 255.toChar()
const val DECORATOR_INT_ID = 254.toChar()
const val DECORATOR_END_ID = 253.toChar()
const val SINGLE_PIXEL_CHAR = 252.toChar()

const val RED_WEIGHT = 0.3
const val GREEN_WEIGHT = 0.59
const val BLUE_WEIGHT = 0.11

const val CHAR_MAX:Int = 255
const val CHAR_BEGIN: Int = 32
const val PIXEL_INT_RANGE: Int = 64
const val VALUE_INT_BEGIN: Int = CHAR_BEGIN+PIXEL_INT_RANGE+1
const val MAX_DIMENSION: Int = CHAR_MAX-5-VALUE_INT_BEGIN
const val INT_RAW_MAX: Int = CHAR_MAX-CHAR_BEGIN
class Constants {
}