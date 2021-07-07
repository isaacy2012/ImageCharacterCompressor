package utils.converters

import MAX_DIMENSION
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import images.CompressibleImage
import images.Image
import java.util.*

private fun findMaxSizeSquare(image: Image, filled: Array<Array<Boolean>>, row: Int, col: Int): Int {
    val pixelColor = image.data[row][col]!!.color
    var dimension = 1
    while (true) {
        if (row+dimension >= image.height || col+dimension >= image.width) {
            return dimension
        }
        for (y in row..row+dimension) {
            val pixel = image.data[y][col+dimension]
            if (filled[y][col+dimension] || pixel == null || pixel.color != pixelColor) {
                return dimension
            }
        }
        for (x in col until col+dimension) {
            val pixel = image.data[row+dimension][x]
            if (filled[row+dimension][x] || pixel == null || pixel.color != pixelColor) {
                return dimension
            }
        }
        dimension++
    }
}

private fun pow2(x: Int): Int {
    return x * x
}

/**
 * Converts an image to a compressibleImage
 * @param image the Image
 */
fun imageToCompressibleImage(image: Image): CompressibleImage {
// TODO add logic for detecting and adding squares
    val filled: Array<Array<Boolean>> = Array(image.height) { Array(image.width){false} }
    val data = ArrayList<ImageObject>()
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            if (filled[row][col]) { continue }

            val squareSize = findMaxSizeSquare(image, filled, row, col)
            val square = Square(squareSize, image.data[row][col]!!)
            if (1 + (square.dimension % MAX_DIMENSION.toInt()) + 1 < pow2(squareSize) ) {
                data.add(square)
                for (dRow in 0 until squareSize) {
                    for (dCol in 0 until squareSize) {
                        filled[row+dRow][col+dCol] = true
                    }
                }
            } else {
                data.add(image.data[row][col]!!)
                filled[row][col] = true
            }
        }
    }
    return CompressibleImage(image.width, image.height, data)
}

fun imageToCompressibleImageNoSquares(image: Image): CompressibleImage {
    val data = ArrayList<ImageObject>()
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            image.data[row][col]?.let { data.add(it) }
        }
    }
    return CompressibleImage(image.width, image.height, data)
}

/**
 * Converts Compressible Image to normal Image
 * @param cimage the CompressibleImage
 */
fun compressibleImageToImage(cimage: CompressibleImage): Image {
    val newData: Array<Array<Pixel?>> = Array(cimage.height) { arrayOfNulls<Pixel>(cimage.width) }
    var row = 0;
    var col = 0;
    val dataQueue = ArrayDeque(cimage.data)
    //pop from queue
    while (dataQueue.isEmpty() == false && row < cimage.height) {
        val temp = dataQueue.pop()
        // if current [row][col] has already been filled by a square
        // keep going through the scan until there is an unfilled pixel space
        while (newData[row][col] != null) {
            col++
            if (col == cimage.width) {
                row++
                col = 0
                if (row == cimage.height ) {
                    break;
                }
            }
        }
        //add to the array if single pixel
        if (temp is Pixel) {
            newData[row][col] = temp
        } else if (temp is Square) {
            // fill each pixel space in the square
            for (dRow in 0 until temp.dimension) {
                for (dCol in 0 until temp.dimension) {
                    newData[row+dRow][col+dCol] = temp.pixel
                }
            }
        }
        //after each, continue scan
        col++
        // wrap around at width
        if (col == cimage.width) {
            row++
            col = 0
            if (row == cimage.height ) {
                break;
            }
        }
    }
    return Image(cimage.width, cimage.height, newData)
}
