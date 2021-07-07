package tests

import CHAR_BEGIN
import CHAR_MAX
import LAST
import PIXEL_MAX_DEPTH
import VALUE_INT_BEGIN
import VALUE_INT_BEGIN_SP
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import images.EncodableImage
import org.junit.jupiter.api.Assertions
import java.util.*

internal fun encode_decode_test(width: Int, height: Int, data: ArrayList<ImageObject>) {
    val image = EncodableImage(width, height, data)
    val charArray = image.encode()
    val decoded = EncodableImage.fromCharArray(charArray)

    Assertions.assertEquals(width, decoded.width)
    Assertions.assertEquals(height, decoded.height)
    Assertions.assertEquals(image.toString(), decoded.toString())
}

internal fun CharArray.toHumanReadable(): String {
   return this.map { parseChar(it) }.toTypedArray().contentToString()
}


internal fun EncodableImage.debugToString(): String {
   val newData: Array<Array<String?>> = Array(height) { arrayOfNulls<String>(width) }
   var row = 0;
   var col = 0;
   val dataQueue = ArrayDeque(data)
   //pop from queue
   while (dataQueue.isEmpty() == false && row < height) {
      val temp = dataQueue.pop()
      // if current [row][col] has already been filled by a square
      // keep going through the scan until there is an unfilled pixel space
      while (newData[row][col] != null) {
         col++
         if (col == width) {
            row++
            col = 0
            if (row == height ) {
               break;
            }
         }
      }
      //add to the array if single pixel
      if (temp is Pixel) {
         newData[row][col] = " $temp "
      } else if (temp is Square) {
         // fill each pixel space in the square
         for (dRow in 0 until temp.dimension) {
            for (dCol in 0 until temp.dimension) {
               newData[row+dRow][col+dCol] = "["+temp.pixel.toString()+"]"
            }
         }
      }
      //after each, continue scan
      col++
      // wrap around at width
      if (col == width) {
         row++
         col = 0
         if (row == height ) {
            break;
         }
      }
   }
   val sb = StringBuilder()
   sb.append(newData.joinToString("\n"){
      val innersb = StringBuilder()
      innersb.append(it.joinToString(""))
      innersb.toString()
   })
   return (sb.toString())
}

/**
 * Convert ICC Char into human-readable form
 */
internal fun parseChar(ch: Char): String {
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

