import java.lang.Math.pow
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

private const val MAX_DIMENSION_DOUBLE: Double = 125.0
private const val MAX_DIMENSION_INT: Int = 125
class CharacterEncoder {

    companion object {
        fun encodeInt(x: Int): Char {
            return x.toChar();
        }

//        fun encodeDimension(x: Int): List<Char> {
//            val ret = ArrayList<Char>()
//            if (x <= 125) {
//                ret.add(encodeInt(x))
//                return ret
//            } else {
////                ret.add(encodeInt(x/MAX_DIMENSION_INT))
//                var dx = x
//                while (dx > MAX_DIMENSION_INT) {
//                    val digit = floor(ln(dx.toDouble()) / ln(MAX_DIMENSION_DOUBLE)).toInt()
//                    val divider = MAX_DIMENSION_INT.toDouble().pow(digit.toDouble()).toInt()
//                    ret.add(encodeInt(digit))
//                    dx = dx % divider
//                }
//                if (dx != 0) {
//                    ret.add(encodeInt(dx))
//                }
//                return ret
//            }
//        }

        fun encodeDimension(x: Int): List<Char> {
            // take the given number
            // convert it into decimal
            // divide the decimal with the target base
            var dx = x;

            // must be in decimal
            val ret = ArrayList<Char>()
            while (dx != 0) {
                val dxValue = dx % MAX_DIMENSION_INT;
                ret.add(0, encodeInt(dxValue));
                dx /= MAX_DIMENSION_INT;
            }
            return ret;
        }
    }

    //Dimension
    //32-159 not 127
    //0-125


}