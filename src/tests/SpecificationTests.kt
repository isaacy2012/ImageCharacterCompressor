package tests

import MAX_DIMENSION
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import images.CompressibleImage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Some or all of these tests may fail when the specification is changed
 */
internal class SpecificationTests {

    @Test
    fun encode_image_test_01() {
        val data = arrayListOf(
                Pixel(0), Pixel(1), Pixel(2),
                Pixel(1), Square(2, Pixel(0)),
                Pixel(3))
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "0,1", "2,1", "v2", "0,3").contentToString()
        val actual = image.compress().toHumanReadable()
        Assertions.assertEquals(expected, actual)
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
        val expected = arrayOf("vsp3", "vsp3", "1,0", "vsp2", "1,1", "3,2", "1,0").contentToString()
        val actual = image.compress().toHumanReadable()
        Assertions.assertEquals(expected, actual)
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
        val actual = image.compress().toHumanReadable()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_04() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(3, Pixel(0))
        )
        val image = CompressibleImage(3, 3, data)
        val expected = arrayOf("vsp3", "vsp3", "vsp3", "0,0").contentToString()
        val actual = image.compress().toHumanReadable()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun encode_image_test_05() {
        val data: ArrayList<ImageObject> = arrayListOf(
                Square(300, Pixel(2))
        )
        val image = CompressibleImage(300, 300, data)
        val expected = arrayOf("vsp3", "v72", "vsp3", "v72", "vsp3", "v" + (300 - 3 * MAX_DIMENSION.toInt()), "2,0").contentToString()
        val actual = image.compress().toHumanReadable()
        Assertions.assertEquals(expected, actual)
    }
}