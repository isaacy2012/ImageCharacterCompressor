package imageObjects

import utils.encodeDimension


data class Point(val x: Int, val y: Int): EncodableImageObject {
    override fun encode(): CharArray {
        return x.encodeDimension().plus(y.encodeDimension())
    }

}