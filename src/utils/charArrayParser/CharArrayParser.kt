package utils.charArrayParser

import CompressibleImage
import LAST
import VALUE_INT_BEGIN
import VALUE_INT_BEGIN_SP
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import imageObjects.getPixelPairFromIntChar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertTrue

fun parseCharArray(arr: CharArray): CompressibleImage {
    val queue = ArrayDeque(arr.toList())
    val data = ArrayList<ImageObject>()
    val width = parseDimension(queue)
    val height = parseDimension(queue)



    while (queue.isEmpty() == false) {
        val intch = queue.pop().toInt()
        when {
            intch < VALUE_INT_BEGIN ->  {
                data.addAll(getPixelPairFromIntChar(intch))
            }
            intch < VALUE_INT_BEGIN_SP -> {
                val dimension: Int = parseDimension(queue)
                val pixels: Array<Pixel> = parsePixel(queue)
                data.add(Square(dimension, pixels[0]))
                data.add(pixels[1])
            }
            intch < LAST -> {
                assertTrue(data.size > 0)
                assertTrue(data[data.size-1] is Pixel)
                // single pixel-breaking value (dimension)
                data.removeAt(data.size-1)
                val dimension: Int = parseDimension(queue)
                val pixels: Array<Pixel> = parsePixel(queue)
                data.add(Square(dimension, pixels[0]))
                data.add(pixels[1])
            }

        }
    }
    TODO()
}

/**
 * Parses the dimension from a queue, stopping when it reaches
 * a COMMA character or a non-VALUE character.
 * @param queue the queue containing the compressed characters
 */
fun parseDimension(queue: ArrayDeque<Char>): Int {
    TODO()
}

fun parsePixel(queue: ArrayDeque<Char>): Array<Pixel> {
    val intch = queue.pop().toInt()
    return getPixelPairFromIntChar(intch)
}
