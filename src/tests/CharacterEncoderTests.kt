package tests

import CHAR_BEGIN
import CHAR_MAX
import CompressibleImage
import INT_RAW_MAX
import MAX_DIMENSION
import PIXEL_MAX_DEPTH
import SINGLE_PIXEL_CHAR
import VALUE_INT_BEGIN
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import utils.encodeInt
import java.util.*

internal class CharacterEncoderTests {
    //RANGE 0-125

    @Test
    fun encode_raw_int_test() {
        for (i in 0..INT_RAW_MAX - CHAR_BEGIN) {
            assertEquals(i+CHAR_BEGIN, encodeInt(i).toInt())
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
    fun encode_image_test_01() {
        val data = arrayListOf(
                Pixel(0), Pixel(1), Pixel(2),
                Pixel(1), Square(2, Pixel(0)),
                Pixel(3))
        val image = CompressibleImage(3, 3, data)
        val expected = arrayStringOf(1,17,99-32, 3)
        val actual = image.compress().map{ it.toInt()}.toIntArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_02() {
        val data = arrayListOf(
                Square(2, Pixel(0)), Pixel(1),
                Pixel(2), Pixel(1),
                Pixel(2),
                Pixel(3)
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayStringOf(99-32, 1, 17, 19)
        val actual = Arrays.toString(image.compress().map{ it.toInt()}.toIntArray())
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_03() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(3, Pixel(0))
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayStringOf(100-32, 0, 252-32)
        val actual = image.compress().map { it.toInt() }.toIntArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_04() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(300, Pixel(2))
        )
        val image = CompressibleImage(300, 300, data)
        val maxdim = MAX_DIMENSION
        println(maxdim)
        val expected = arrayStringOf(98-32, 244-32, 16, 252-32)
        val actual = image.compress().map { it.toInt() }.toIntArray().contentToString()
        assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_05() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(300, Pixel(2))
        )
        val image = CompressibleImage(300, 300, data)
        val expected = arrayOf("v1", "v147", "2,0", "SINGLE_PIXEL_CHAR").contentToString()
        val actual = image.compress().map { parseChar(it) }.toTypedArray().contentToString()
        assertEquals(expected, actual)
    }

    private fun arrayStringOf(vararg elements: Int): String {
        return elements.map { CHAR_BEGIN + it }.toIntArray().contentToString()
    }

    private fun parseChar(ch: Char): String {
        val intch = ch.toInt()
        assertTrue(intch >= CHAR_BEGIN)
        assertTrue(intch <= CHAR_MAX)
        when {
            intch < VALUE_INT_BEGIN -> {
                val left = (((intch-CHAR_BEGIN) ushr 3) or 0).toString()
                val right = ((intch-CHAR_BEGIN) and PIXEL_MAX_DEPTH).toString()
                return left + "," + right
            }
            intch < SINGLE_PIXEL_CHAR.toInt() -> {
                return "v" + (intch-VALUE_INT_BEGIN).toString()
            }
            else -> {
                when (intch) {
                    255 -> {
                        return "DECORATOR_START_ID"
                    }
                    254 -> {
                        return "DECORATOR_INT_ID"
                    }
                    253 -> {
                        return "DECORATOR_END_ID"
                    }
                    252 -> {
                        return "SINGLE_PIXEL_CHAR"
                    }
                    else -> {
                        return "r" + intch.toString()
                    }
                }
            }
        }
    }


}

