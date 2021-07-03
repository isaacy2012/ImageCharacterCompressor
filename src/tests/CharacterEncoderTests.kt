package tests

import CHAR_BEGIN
import INT_RAW_MAX
import PIXEL_INT_RANGE
import PIXEL_MAX_DEPTH
import imageObjects.Pixel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.encodeRawInt

internal class CharacterEncoderTests {
    //RANGE 0-125

    @Test
    fun encode_raw_int_test() {
        for (i in 0..INT_RAW_MAX-CHAR_BEGIN) {
            assertEquals(i+CHAR_BEGIN, encodeRawInt(i).toInt())
        }
    }

    @Test
    fun encode_pixel_int_test() {
        var counter = 0
        for (i in 0..PIXEL_MAX_DEPTH) {
            for (j in 0..PIXEL_MAX_DEPTH) {
                assertEquals(counter+CHAR_BEGIN, Pixel(i).compressWithNext(Pixel(j)).toInt())
                counter++
            }
        }
    }

}