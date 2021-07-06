package utils.charArrayParser

import CompressibleImage
import MAX_DIMENSION
import VALUE_INT_BEGIN
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import imageObjects.getPixelPairFromChar
import isNonSPValue
import isPixel
import isSPValue
import utils.fromSP
import java.util.*
import kotlin.test.assertTrue

fun parseCharArray(arr: CharArray): CompressibleImage {
    val queue = ArrayDeque(arr.toList())
    val data = ArrayList<ImageObject>()
    val width = parseDimension(queue)
    val height = parseDimension(queue)
    val wh = width * height

    var counter = 0
    while (queue.isEmpty() == false && counter < wh) {
        val nextCh = queue.peek()
        when {
            nextCh.isPixel() -> {
                data.addAll(parsePixelPair(queue))
                counter += 2
            }
            //non SP Value or it is the first Square
            nextCh.isNonSPValue() || (nextCh.isSPValue() && data.isEmpty()) -> {
                val dimension: Int = parseDimension(queue)
                val pixels: Array<Pixel> = parsePixelPair(queue)
                data.add(Square(dimension, pixels[0]))
                counter += pow2(dimension)
                data.add(pixels[1])
                counter++
            }
            nextCh.isSPValue() -> {
                assertTrue(data.size > 0)
                assertTrue(data[data.size - 1] is Pixel)
                // single pixel-breaking value (dimension)
                data.removeAt(data.size - 1) as Pixel
                counter--
                val dimension: Int = parseDimension(queue)
                val pixels: Array<Pixel> = parsePixelPair(queue)
                data.add(Square(dimension, pixels[0]))
                counter += pow2(dimension)
                if (counter < wh) {
                    data.add(pixels[1])
                    counter++
                }
            }
            else -> {
                println("Error at $nextCh")
            }

        }
    }
    return CompressibleImage(width, height, data)
}

private fun pow2(x: Int): Int {
    return x * x
}

private fun parsePixelPair(queue: ArrayDeque<Char>): Array<Pixel> {
    return getPixelPairFromChar(queue.pop())
}

/**
 * Parses the dimension from a queue, stopping when it reaches
 * a COMMA character or a non-VALUE character.
 * @param queue the queue containing the compressed characters
 */
private fun parseDimension(queue: ArrayDeque<Char>): Int {
    val intList = ArrayList<Int>()
    val first = queue.pop()
    if (first.isSPValue()) {
        intList.add(first.fromSP().toInt() - VALUE_INT_BEGIN.toInt())
    } else {
        assertTrue(first.isNonSPValue())
        intList.add(first.toInt() - VALUE_INT_BEGIN.toInt())
    }
//    intList.add((queue.pop().toInt()-VALUE_INT_BEGIN.toInt()))
    while (queue.isEmpty() == false && queue.peek().isNonSPValue()) {
        intList.add((queue.pop().toInt() - VALUE_INT_BEGIN.toInt()))
    }


    var ret = 0
    var pow = 1
    for (i in intList.size - 1 downTo 0) {
        ret += intList[i] * pow
        pow *= MAX_DIMENSION.toInt()
    }
    return ret
}
