import exceptions.ImageSizeException
import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.ArrayList
import javax.imageio.ImageIO

class Image(fileName: String) {
    private var width: Int = 0
    private var height: Int = 0

    lateinit var data: Array<Array<String>>

    private fun convertRgbToGreyscale(rgb: Int): Int {
        val pixel = Color(rgb)

        var ret = (K * (RED_WEIGHT * pixel.red + GREEN_WEIGHT * pixel.green + BLUE_WEIGHT * pixel.blue)).toInt()
        if (ret > PIXEL_MAX_DEPTH) {
            ret = PIXEL_MAX_DEPTH
        }
        return ret
    }


    init {
        try {
            val dataStream = ArrayList<String>()
            val img = ImageIO.read(File(fileName))
            this.width = img.width
            this.height = img.height
            if (width * height > MAX_PIXELS) {
                println("Error, photo too large. Please use an image with no more than 900 pixels.")
                throw ImageSizeException("bad bad bad");
            }
            dataStream.add(width.toString())
            dataStream.add(height.toString())

            //Scanline
            data = Array(height) { Array(width) { String() } }
            for (i_height in 0 until height) {
                for (i_width in 0 until width) {
                    val grayColor = convertRgbToGreyscale(img.getRGB(i_width, i_height))
                    data[i_height][i_width] = grayColor.toString()
                }
            }
        } catch (e: IOException) {
            println("error $e")
        }
    }
}
