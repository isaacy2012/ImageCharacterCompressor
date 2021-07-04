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
import utils.encodeInt
import java.util.*

internal class CharacterEncoderTests {

    @Test
    fun encode_raw_int_test() {
        for (i in 0..INT_RAW_MAX - CHAR_BEGIN) {
            assertEquals(i + CHAR_BEGIN, encodeInt(i).toInt())
        }
    }

    @Test
    fun encode_pixel_int_test() {
        var counter = 0
        for (i in 0..PIXEL_MAX_DEPTH) {
            for (j in 0..PIXEL_MAX_DEPTH) {
                assertEquals(counter + CHAR_BEGIN, Pixel(i).compressWithNext(Pixel(j)).toInt())
                counter++
            }
        }
    }

    @Test
    fun nocollisions() {
        val checked = HashSet<Int>()
        checked.add(LAST)
        for (i in VALUE_INT_BEGIN..VALUE_INT_BEGIN+MAX_DIMENSION) {
            assertFalse(checked.contains(i))
            checked.add(i)
            assertEquals("v" + (i-VALUE_INT_BEGIN), parseChar(i.toChar()))
        }
        for (i in VALUE_INT_BEGIN_SP..VALUE_INT_BEGIN_SP+MAX_DIMENSION) {
            assertFalse(checked.contains(i))
            assertEquals("vsp" + (i-VALUE_INT_BEGIN_SP), parseChar(i.toChar()))
        }
    }

    @Test
    fun encode_image_test_01() {
        val data = arrayListOf(
                Pixel(0), Pixel(1), Pixel(2),
                Pixel(1), Square(2, Pixel(0)),
                Pixel(3))
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("0,1", "2,1", "v2", "0,3").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
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
        val expected = arrayOf("1,0", "vsp2", "1,1", "3,2", "1,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
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
        val expected = arrayOf("v2", "0,1", "2,1", "2,3").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
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
        val expected = arrayOf("v3", "0,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_05() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(300, Pixel(2))
        )
        val image = CompressibleImage(300, 300, data)
        val expected = arrayOf("v3", "v"+(300-3*MAX_DIMENSION), "2,0").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_from_file() {
        val image = Image("samd.png")
        print(image.toString())
        println("==========================================")
        val smaller = image.resizeToWidth(20)
        print(smaller.toString())
        println("==========================================")
        val compressible = CompressibleImage.of(smaller)
        val actual = compressible.compress().map { parseChar(it) }.toTypedArray().contentToString()
        println(actual)
        assertEquals(smaller.toString(), compressible.toString())
    }

    /**
     * Convert ICC Char into human-readable form
     */
    private fun parseChar(ch: Char): String {
        val intch = ch.toInt()
        assertTrue(intch >= CHAR_BEGIN)
        assertTrue(intch <= CHAR_MAX)
        when {
            intch < VALUE_INT_BEGIN -> {
                // Pixel pair
                val left = (((intch - CHAR_BEGIN) ushr 3) or 0).toString()
                val right = ((intch - CHAR_BEGIN) and PIXEL_MAX_DEPTH).toString()
                return "$left,$right"
            }
            intch < VALUE_INT_BEGIN_SP -> {
                // value (dimension)
                return "v" + (intch - VALUE_INT_BEGIN).toString()
            }
            intch < LAST -> {
                // single pixel-breaking value (dimension)
                return "vsp" + (intch - (VALUE_INT_BEGIN_SP)).toString()
            }
            else -> {
                // Special codes
                return when (intch) {
                    255 -> {
                        "DECORATOR_START_ID"
                    }
                    254 -> {
                        "DECORATOR_INT_ID"
                    }
                    253 -> {
                        "DECORATOR_END_ID"
                    }
                    else -> {
                        "r$intch"
                    }
                }
            }
        }
    }


}

