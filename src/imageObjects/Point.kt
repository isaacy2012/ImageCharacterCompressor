package imageObjects

import utils.compressDimension


data class Point(val x: Int, val y: Int): CompressibleImageObject {
    override fun compress(): CharArray {
        return x.compressDimension().plus(y.compressDimension())
    }

}