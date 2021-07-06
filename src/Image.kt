
import exceptions.ImageSizeException
import imageObjects.Pixel
import imageObjects.Square
import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

private fun convertRgbToGrayscale(rgb: Int): Int {
    val pixel = Color(rgb)

    var ret = (K * (RED_WEIGHT * pixel.red + GREEN_WEIGHT * pixel.green + BLUE_WEIGHT * pixel.blue)).toInt()
    if (ret > PIXEL_MAX_DEPTH) {
        ret = PIXEL_MAX_DEPTH
    }
    return ret
}

class Image() {

    companion object {
        fun of(cimage: CompressibleImage): Image {
            val newData: Array<Array<Pixel?>> = Array(cimage.height) { arrayOfNulls<Pixel>(cimage.width) }
            var row = 0;
            var col = 0;
            val dataQueue = ArrayDeque(cimage.data)
            //pop from queue
            while (dataQueue.isEmpty() == false && row < cimage.height) {
                // wrap around at width
                if (col == cimage.width) {
                    row++
                    col = 0
                    if (row == cimage.height ) {
                        break;
                    }
                }
                val temp = dataQueue.pop()
                //add to the array if single pixel
                if (temp is Pixel) {
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
            }
            return Image(cimage.width, cimage.height, newData)
        }
    }

    var width: Int = 0
    var height: Int = 0

    lateinit var data: Array<Array<Pixel?>>

    constructor(width: Int, height: Int, data: Array<Array<Pixel?>>) : this() {
        this.width = width
        this.height = height
        this.data = data
    }

    constructor(fileName: String) : this() {
        try {
            val img = ImageIO.read(File(fileName))
            this.width = img.width
            this.height = img.height
            if (width * height > MAX_PIXELS) {
                throw ImageSizeException("Error, photo too large. Please use an image with no more than 900 pixels.");
            }

            //Scanline
            data = Array(height) { arrayOfNulls<Pixel>(width) }
            for (i_height in 0 until height) {
                for (i_width in 0 until width) {
                    val grayColor = convertRgbToGrayscale(img.getRGB(i_width, i_height))
                    data[i_height][i_width] = Pixel(grayColor)
                }
            }
        } catch (e: IOException) {
            println("error $e")
        }
    }

    private fun calculateAverage(row: Int, col: Int, rowSearch: Double, colSearch: Double): Int {
        var total = 0
        val rowStart = (row*rowSearch).roundToInt()
        val colStart = (col*colSearch).roundToInt()
//        println("height: $height width: $width rowStart: $rowStart colStart: $colStart")
        for (dRow in rowStart until (rowStart+rowSearch).toInt()) {
            for (dCol in colStart until (colStart+colSearch).toInt()) {
                val pixel = data[dRow][dCol]
                if (pixel != null) {
                    total += pixel.color
                }
            }
        }
        return (total/(rowSearch*colSearch)).roundToInt()

    }

    fun resizeToWidth(newWidth: Int): Image {
        val newHeight = (newWidth / width.toDouble() * height).toInt()
        val newData = Array(newHeight) { arrayOfNulls<Pixel>(newWidth) }
        val rowSearch = height/newHeight.toDouble()
        val colSearch = width/newWidth.toDouble()
        for (row in 0 until newHeight) {
            for (col in 0 until newWidth) {
                newData[row][col] = Pixel(calculateAverage(row, col, rowSearch, colSearch))
            }
        }
        return Image(newWidth, newHeight, newData)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(this.data.joinToString("\n"){
            val innersb = StringBuilder()
            innersb.append(it.joinToString(" "))
            innersb.toString()
        })
        return (sb.toString())
    }

}

