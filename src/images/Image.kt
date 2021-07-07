package images
import BLUE_WEIGHT
import GREEN_WEIGHT
import K
import PIXEL_MAX_DEPTH
import RED_WEIGHT
import imageObjects.Pixel
import utils.converters.compressibleImageToImage
import java.awt.Color
import java.io.File
import java.io.IOException
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
            return compressibleImageToImage(cimage)
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

