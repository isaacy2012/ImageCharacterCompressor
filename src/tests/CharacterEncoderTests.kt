package tests

import CHAR_BEGIN
import CHAR_MAX
import CompressibleImage
import INT_RAW_MAX
import Image
import LAST
import MAX_DIMENSION
import PIXEL_MAX_DEPTH
import VALUE_INT_BEGIN
import VALUE_INT_BEGIN_SP
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.charArrayParser.parseDimension
import utils.compressDimension
import utils.compressDimensionSP
import utils.encodeInt
import java.util.*
import kotlin.math.abs

internal class CharacterEncoderTests {

    private fun encode_decode_test(width: Int, height: Int, data: ArrayList<ImageObject>) {
        val image = CompressibleImage(width, height, data)
        val charArray = image.compress()
        val decoded = CompressibleImage.fromCharArray(charArray)

        assertEquals(width, decoded.width)
        assertEquals(height, decoded.height)
        assertEquals(image.toString(), decoded.toString())
    }

    @Test
    fun encode_raw_int_test() {
        for (i in 0..INT_RAW_MAX - CHAR_BEGIN) {
            assertEquals(i + CHAR_BEGIN.toInt(), encodeInt(i).toInt())
        }
    }

    fun consts() {
        println(0.compressDimension()[0].toInt())
        println(0.compressDimensionSP()[0].toInt())
    }

    @Test
    fun encode_pixel_int_test() {
        var counter = 0
        for (i in 0..PIXEL_MAX_DEPTH) {
            for (j in 0..PIXEL_MAX_DEPTH) {
                assertEquals(counter + CHAR_BEGIN.toInt(), Pixel(i).compressWithNext(Pixel(j)).toInt())
                counter++
            }
        }
    }

    @Test
    fun encode_dimension_test() {
        val random = Random(1)
        for (i in 0..1000) {
            val num = abs(random.nextInt())
            val list = num.compressDimension().toList()
            assertEquals(num, parseDimension(ArrayDeque(list)))
            val list2 = num.compressDimensionSP().toList()
            assertEquals(num, parseDimension(ArrayDeque(list2)))
        }
    }

    @Test
    fun nocollisions() {
        val checked = HashSet<Char>()
        checked.add(LAST)
        for (i in VALUE_INT_BEGIN..VALUE_INT_BEGIN + MAX_DIMENSION.toInt()) {
            assertFalse(checked.contains(i))
            checked.add(i)
            assertEquals("v" + (i - VALUE_INT_BEGIN), parseChar(i))
        }
        for (i in VALUE_INT_BEGIN_SP..VALUE_INT_BEGIN_SP + MAX_DIMENSION.toInt()) {
            assertFalse(checked.contains(i))
            assertEquals("vsp" + (i - VALUE_INT_BEGIN_SP), parseChar(i))
        }
    }

    @Test
    fun encode_image_test_01() {
        val data = arrayListOf(
                Pixel(0), Pixel(1), Pixel(2),
                Pixel(1), Square(2, Pixel(0)),
                Pixel(3))
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "0,1", "2,1", "v2", "0,3").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_decode_image_test_single_pixel() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Pixel(0)
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp1", "vsp1", "1,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
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

    /**
     * 1 2 2
     * 1 2 2
     * 3 2 1
     */
    @Test
    fun encode_image_test_02() {
        val data = arrayListOf(
                Pixel(1), Square(2, Pixel(1)),
                Pixel(1),
                Pixel(3), Pixel(2), Pixel(1)
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "1,1", "vsp2", "1,3", "2,1").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
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

    /**
     * 0 0 1
     * 0 0 2
     * 1 2 3
     */
    @Test
    fun encode_image_test_03() {
        val data = arrayListOf(
                Square(2, Pixel(0)), Pixel(1),
                Pixel(2), Pixel(1),
                Pixel(2),
                Pixel(3)
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "vsp2", "0,1", "2,1", "2,3").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
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

    @Test
    fun encode_image_test_04() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(3, Pixel(0))
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "vsp3", "0,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_05() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(300, Pixel(2))
        )
        val image = CompressibleImage(300, 300, data)
        val expected = arrayOf("vsp3", "v72", "vsp3", "v72", "vsp3", "v" + (300 - 3 * MAX_DIMENSION.toInt()), "2,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
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

    @Test
    fun encode_image_from_file() {
        val image = Image("samd.png")
        val smaller = image.resizeToWidth(20)
        val compressible = CompressibleImage.of(smaller)
        val actual = compressible.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(smaller.toString(), compressible.toString())
    }

    /**
     * Convert ICC Char into human-readable form
     */
    private fun parseChar(ch: Char): String {
        assertTrue(ch >= CHAR_BEGIN)
        assertTrue(ch <= CHAR_MAX)
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


}

