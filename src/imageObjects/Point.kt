package imageObjects

import Compressible
import utils.compressInt

data class Point(val x: Int, val y: Int): CompressibleImageObject {
    override fun compress(): CharArray {
        return compressInt(x).plus(compressInt(y))
    }

}