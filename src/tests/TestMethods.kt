package tests

import CHAR_BEGIN
import CHAR_MAX
import CompressibleImage
import LAST
import PIXEL_MAX_DEPTH
import VALUE_INT_BEGIN
import VALUE_INT_BEGIN_SP
import imageObjects.ImageObject
import org.junit.jupiter.api.Assertions

fun encode_decode_test(width: Int, height: Int, data: ArrayList<ImageObject>) {
    val image = CompressibleImage(width, height, data)
    val charArray = image.compress()
    val decoded = CompressibleImage.fromCharArray(charArray)

    Assertions.assertEquals(width, decoded.width)
    Assertions.assertEquals(height, decoded.height)
    Assertions.assertEquals(image.toString(), decoded.toString())
}

fun CharArray.toHumanReadable(): String {
   return this.map { parseChar(it) }.toTypedArray().contentToString()
}

/**
 * Convert ICC Char into human-readable form
 */
fun parseChar(ch: Char): String {
   Assertions.assertTrue(ch >= CHAR_BEGIN)
   Assertions.assertTrue(ch <= CHAR_MAX)
   when {
      ch < VALUE_INT_BEGIN -> {
         // Pixel pair
         val left = (((ch - CHAR_BEGIN) ushr 3) or 0).toString()
         val right = ((ch - CHAR_BEGIN) and PIXEL_MAX_DEPTH).toString()
         return "$left,$right"
      }
      ch < VALUE_INT_BEGIN_SP -> {
         // value (dimension)
         return "v" + (ch - VALUE_INT_BEGIN).toString()
      }
      ch < LAST -> {
         // single pixel-breaking value (dimension)
         return "vsp" + (ch - (VALUE_INT_BEGIN_SP)).toString()
      }
      else -> {
         // Special codes
         return when (ch) {
            255.toChar() -> {
               "DECORATOR_START_ID"
            }
            254.toChar() -> {
               "DECORATOR_INT_ID"
            }
            253.toChar() -> {
               "DECORATOR_END_ID"
            }
            252.toChar() -> {
               "COMMA"
            }
            else -> {
               "r$ch"
            }
         }
      }
   }
}

