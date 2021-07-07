package tests

import CHAR_BEGIN
import INT_RAW_MAX
import LAST
import MAX_DIMENSION
import PIXEL_MAX_DEPTH
import VALUE_INT_BEGIN
import VALUE_INT_BEGIN_SP
import imageObjects.Pixel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.encodeDimension
import utils.encodeDimensionSP
import utils.encodeInt
import java.util.*
import kotlin.math.abs
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinFunction

/**
 * Tests the encoding/decoding of values
 */
internal class ValueTests {

    @Test
    fun encode_raw_int_test() {
        for (i in 0..INT_RAW_MAX - CHAR_BEGIN) {
            Assertions.assertEquals(i + CHAR_BEGIN.toInt(), encodeInt(i).toInt())
        }
    }

    fun consts() {
        println(0.encodeDimension()[0].toInt())
        println(0.encodeDimensionSP()[0].toInt())
    }

    @Test
    fun encode_pixel_int_test() {
        var counter = 0
        for (i in 0..PIXEL_MAX_DEPTH) {
            for (j in 0..PIXEL_MAX_DEPTH) {
                Assertions.assertEquals(counter + CHAR_BEGIN.toInt(), Pixel(i).compressWithNext(Pixel(j)).toInt())
                counter++
            }
        }
    }

    @Test
    fun encode_dimension_test() {
        val random = Random(1)
        for (i in 0..1000) {
            val num = abs(random.nextInt())
            val list = num.encodeDimension().toList()

            //using reflection to access private function
            val c = Class.forName("utils.charArrayParser.CharArrayParserKt")
            val parseDimensionKF = c.getDeclaredMethod("parseDimension", ArrayDeque::class.java).kotlinFunction!!
            parseDimensionKF.isAccessible = true

            Assertions.assertEquals(num, parseDimensionKF.call(ArrayDeque(list)))
            val list2 = num.encodeDimensionSP().toList()
            Assertions.assertEquals(num, parseDimensionKF.call(ArrayDeque(list2)))
        }
    }

    @Test
    fun encode_dimension_multi_test() {
        val random = Random(1)
        val expected = ArrayList<Int>()
        val charArrayList = ArrayList<Char>()
        for (i in 0..1000) {
            val num = abs(random.nextInt())
            expected.add(num)
            val list = num.encodeDimensionSP().toList()
            charArrayList.addAll(list)
        }
        val deque = ArrayDeque(charArrayList)

        //using reflection to access private function
        val c = Class.forName("utils.charArrayParser.CharArrayParserKt")
        val parseDimensionKF = c.getDeclaredMethod("parseDimension", ArrayDeque::class.java).kotlinFunction!!
        parseDimensionKF.isAccessible = true

        val actual = ArrayList<Int>()
        while (deque.isEmpty() == false) {
            actual.add(parseDimensionKF.call(deque) as Int)
        }
        assertEquals(expected, actual)
    }

    @Test
    fun nocollisions() {
        val checked = HashSet<Char>()
        checked.add(LAST)
        for (i in VALUE_INT_BEGIN..VALUE_INT_BEGIN + MAX_DIMENSION.toInt()) {
            Assertions.assertFalse(checked.contains(i))
            checked.add(i)
            Assertions.assertEquals("v" + (i - VALUE_INT_BEGIN), parseChar(i))
        }
        for (i in VALUE_INT_BEGIN_SP..VALUE_INT_BEGIN_SP + MAX_DIMENSION.toInt()) {
            Assertions.assertFalse(checked.contains(i))
            Assertions.assertEquals("vsp" + (i - VALUE_INT_BEGIN_SP), parseChar(i))
        }
    }
}