
import exceptions.ImageSizeException
import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

private fun convertRgbToGrayscale(rgb: Int): Int {
    val pixel = Color(rgb)

    var ret = (K * (RED_WEIGHT * pixel.red + GREEN_WEIGHT * pixel.green + BLUE_WEIGHT * pixel.blue)).toInt()
    if (ret > PIXEL_MAX_DEPTH) {
        ret = PIXEL_MAX_DEPTH
    }
    return ret
}

class Image(fileName: String) {
    var width: Int = 0
    var height: Int = 0

    lateinit var data: Array<Array<Int?>>

    init {
        try {
            val img = ImageIO.read(File(fileName))
            this.width = img.width
            this.height = img.height
            if (width * height > MAX_PIXELS) {
                throw ImageSizeException("Error, photo too large. Please use an image with no more than 900 pixels.");
            }

            //Scanline
            data = Array(height) { arrayOfNulls<Int>(width) }
            for (i_height in 0 until height) {
                for (i_width in 0 until width) {
                    val grayColor = convertRgbToGrayscale(img.getRGB(i_width, i_height))
                    data[i_height][i_width] = grayColor
                }
            }
        } catch (e: IOException) {
            println("error $e")
        }
    }

    fun resizeToWidth(): Image {
        TODO()
    }

}
