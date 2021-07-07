package tests

import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import images.CompressibleImage
import images.Image
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

/**
 * These integration should pass regardless of whether the specification changes
 */
internal class IntegrationTests {


    @Test
    fun encode_decode_image_test_single_pixel() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Pixel(0)
        )
        val image = CompressibleImage(3, 3, data)
        encode_decode_test(1, 1, data)
    }

    @Test
    fun encode_decode_image_test_01() {
        val data = arrayListOf(
                Pixel(0), Pixel(1), Pixel(2),
                Pixel(1), Square(2, Pixel(0)),
                Pixel(3))
        encode_decode_test(3, 3, data)
    }


    @Test
    fun encode_decode_image_test_02() {
        val data = arrayListOf(
                Pixel(1), Square(2, Pixel(1)),
                Pixel(1),
                Pixel(3), Pixel(2), Pixel(1)
        )
        encode_decode_test(3, 3, data)
    }


    @Test
    fun encode_decode_image_test_03() {
        val data = arrayListOf(
                Square(2, Pixel(0)), Pixel(1),
                Pixel(2), Pixel(1),
                Pixel(2),
                Pixel(3)
        )
        encode_decode_test(3, 3, data)
    }

    @Test
    fun toString_image_test_03() {
        val data = arrayListOf(
                Square(2, Pixel(0)), Pixel(1),
                Pixel(2), Pixel(1),
                Pixel(2),
                Pixel(3)
        )
        val image = CompressibleImage(3, 3, data)
        val expected = "" +
                "0 0 1\n" +
                "0 0 2\n" +
                "1 2 3"
        val actual = image.toString()
        assertEquals(expected, actual)
    }


    /**
     * 0 0 1 2
     * 0 0 1 2
     * 1 1 2 1
     * 1 1 3 4
     */
    @Test
    fun encode_decode_image_test_04() {
        val data = arrayListOf(
                Square(2, Pixel(0)), Pixel(1),
                Pixel(2), Pixel(1),
                Pixel(2),
                Square(2, Pixel(1)),
                Pixel(2), Pixel(1),
                Pixel(3), Pixel(4)
        )
        val image = CompressibleImage(4, 4, data)
        val expected = "" +
                "0 0 1 2\n" +
                "0 0 1 2\n" +
                "1 1 2 1\n" +
                "1 1 3 4"
        val actual = image.toString()
        assertEquals(expected, actual)
        encode_decode_test(4, 4, data)
    }

    /**
     * 0 0 2 2
     * 0 0 2 2
     * 1 1 7 7
     * 1 1 7 7
     */
    @Test
    fun encode_decode_image_test_05() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(2, Pixel(0)),
                Square(2, Pixel(2)),
                Square(2, Pixel(4)),
                Square(2, Pixel(7))
        )
        val image = CompressibleImage(4, 4, data)
        val expected = "" +
                "0 0 2 2\n" +
                "0 0 2 2\n" +
                "4 4 7 7\n" +
                "4 4 7 7"
        val actual = image.toString()
        assertEquals(expected, actual)
        encode_decode_test(4, 4, data)
    }

    @Test
    fun image_to_compressible_test_01() {
        val expected: ArrayList<ImageObject> = arrayListOf(
                Square(4, Pixel(0)),
                Square(4, Pixel(2)),
                Square(4, Pixel(4)),
                Square(4, Pixel(7))
        )
        val imageString =  "" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "4 4 4 4 7 7 7 7\n" +
                "4 4 4 4 7 7 7 7\n" +
                "4 4 4 4 7 7 7 7\n" +
                "4 4 4 4 7 7 7 7"
        val image = imageFromString(8, 8, imageString)
        val compressed = CompressibleImage.of(image)
        assertEquals(expected, compressed.data)
        assertEquals(imageString, compressed.toString())
    }

    @Test
    fun image_to_compressible_test_02() {
        val expected: ArrayList<ImageObject> = arrayListOf(
                Square(4, Pixel(0)),
                Square(4, Pixel(2)),
                Square(4, Pixel(4)),
                Square(3, Pixel(7)),
                Pixel(3),
                Pixel(3),
                Pixel(3),
                Pixel(3),
                Pixel(3),
                Pixel(3),
                Pixel(3)
        )
        val imageString =  "" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "0 0 0 0 2 2 2 2\n" +
                "4 4 4 4 7 7 7 3\n" +
                "4 4 4 4 7 7 7 3\n" +
                "4 4 4 4 7 7 7 3\n" +
                "4 4 4 4 3 3 3 3"
        val image = imageFromString(8, 8, imageString)
        val compressed = CompressibleImage.of(image)
        assertEquals(expected, compressed.data)
        assertEquals(imageString, compressed.toString())
    }

    private fun imageFromString(width: Int, height: Int, str: String): Image {
        val s = Scanner(str.replace("\n", " "))
        val data: Array<Array<Pixel?>> = Array(height) { arrayOfNulls<Pixel>(width) }
        for (row in 0 until height) {
            for (col in 0 until width) {
                data[row][col] = Pixel(s.nextInt())
            }
        }
        return Image(width, height, data)
    }

    @Test
    fun encode_image_from_file() {
        val image = Image("bliss.png")
        val compressible = CompressibleImage.of(image)
        val charArray = compressible.compress()
        val decoded = CompressibleImage.fromCharArray(charArray)
        println(decoded.debugToString())
        assertEquals(compressible.data, decoded.data)

    }



}

