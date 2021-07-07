package utils.converters

import MAX_DIMENSION
import imageObjects.*
import images.EncodableImage
import images.Image
import java.util.*

/**
 * Finds the maximum sized square from this pixel location
 * @param image the image
 * @param filled the array of whether each pixel location has been filled
 * @param row the row
 * @param col the col
 */
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

/**
 * Finds the maximum sized square from this pixel location
 * @param image the image
 * @param filled the array of whether each pixel location has been filled
 * @param row the row
 * @param col the col
 */
private fun findMaxSizeSquare(image: Image, row: Int, col: Int): Int {
    val pixelColor = image.data[row][col]!!.color
    var dimension = 1
    while (true) {
        if (row+dimension >= image.height || col+dimension >= image.width) {
            return dimension
        }
        for (y in row..row+dimension) {
            val pixel = image.data[y][col+dimension]
            if (pixel == null || pixel.color != pixelColor) {
                return dimension
            }
        }
        for (x in col until col+dimension) {
            val pixel = image.data[row+dimension][x]
            if (pixel == null || pixel.color != pixelColor) {
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
 * Converts an image to a encodableImage
 * @param image the Image
 */
fun imageToEncodableImage(image: Image): EncodableImage {
// TODO add logic for detecting and adding squares
    var filled: Array<Array<Boolean>> = Array(image.height) { Array(image.width){false} }
    val data = ArrayList<ImageObject>()
    val pointSquares = ArrayList<PointSquare>()
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            val squareSize = findMaxSizeSquare(image, row, col)
            val pointSquare = PointSquare(col, row, squareSize, image.data[row][col]!!.color)
            if (1 + (squareSize % MAX_DIMENSION.toInt()) + 1 < pow2(squareSize) ) { //could precompile
                pointSquares.add(pointSquare)
            }
        }
    }
    pointSquares.sortWith(reverseOrder())

    val pointToSquare = HashMap<Point, Square>()
    for (pointSquare in pointSquares) {
        var max = pointSquare.square.dimension
        for (dRow in 0 until pointSquare.square.dimension) {
            for (dCol in 0 until pointSquare.square.dimension) {
                if (filled[pointSquare.point.y + dRow][pointSquare.point.x + dCol]) {
                    max = dCol - 1
                }
            }
        }
        if (1 + (max % MAX_DIMENSION.toInt()) + 1 < pow2(max)) { //could precompile
            pointSquare.square.dimension = max
            pointToSquare.put(pointSquare.point, pointSquare.square)
            for (dRow in 0 until max) {
                for (dCol in 0 until max) {
                    filled[pointSquare.point.y+dRow][pointSquare.point.x+dCol] = true
                }
            }
        }
    }

    //reset filled
    filled = Array(image.height) { Array(image.width){false} }
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            if (filled[row][col]) { continue }

            val square = pointToSquare.get(Point(col, row))
            if (square != null) {
                data.add(square)
                for (dRow in 0 until square.dimension) {
                    for (dCol in 0 until square.dimension) {
                        filled[row+dRow][col+dCol] = true
                    }
                }
            } else {
                data.add(image.data[row][col]!!)
            }
        }
    }

    return EncodableImage(image.width, image.height, data)
}

fun imageToEncodableImageNaiveSquare(image: Image):EncodableImage {
    val filled: Array<Array<Boolean>> = Array(image.height) { Array(image.width){false} }
    val data = ArrayList<ImageObject>()
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            if (filled[row][col]) { continue }

            val squareSize = findMaxSizeSquare(image, filled, row, col)
            val square = Square(squareSize, image.data[row][col]!!)
            if (1 + (squareSize % MAX_DIMENSION.toInt()) + 1 < pow2(squareSize) ) {
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
    return EncodableImage(image.width, image.height, data)
}


fun imageToEncodableImageNoCompress(image: Image): EncodableImage {
    val data = ArrayList<ImageObject>()
    for (row in 0 until image.height) {
        for (col in 0 until image.width) {
            image.data[row][col]?.let { data.add(it) }
        }
    }
    return EncodableImage(image.width, image.height, data)
}

/**
 * Converts Encodable Image to normal Image
 * @param cimage the CompressibleImage
 */
fun encodableToImage(cimage: EncodableImage): Image {
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
