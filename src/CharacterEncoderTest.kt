import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import CharacterEncoder.Companion.encodeInt
import CharacterEncoder.Companion.encodeDimension

internal class CharacterEncoderTest {
    //RANGE 0-125

    @Test
    fun encode_int_test() {
        assertEquals('9', encodeInt(57))

    }

    @Test
    fun encode_dimension_test_01() {
        assertEquals(listOf(encodeInt(45)), encodeDimension(45))
    }

    @Test
    fun encode_dimension_test_02() {
        assertEquals(listOf(encodeInt(1), encodeInt(1)), encodeDimension(126))
    }

    @Test
    fun encode_dimension_test_03() {
        assertEquals(listOf(encodeInt(1), encodeInt(0), encodeInt(1)), encodeDimension(15626))
    }

    @Test
    fun encode_dimension_test_04() {
        assertEquals(listOf(encodeInt(1), encodeInt(1), encodeInt(1)), encodeDimension(15751))
    }

    @Test
    fun encode_dimension_test_05() {
        assertEquals(listOf(encodeInt(1), encodeInt(5)), encodeDimension(130))
    }
}