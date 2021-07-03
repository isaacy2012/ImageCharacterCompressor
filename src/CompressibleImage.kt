import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import utils.compressDimension
import utils.compressDimensionSP

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(private var width: Int, private var height: Int, data: ArrayList<ImageObject>) : Compressible {

    companion object {
        fun of(image: Image): CompressibleImage {
            TODO()
            return CompressibleImage(image.width, image.height, ArrayList())
        }
    }

    //compression squares
    var data: ArrayList<ImageObject> = ArrayList()

    init {
        this.data = data
    }

    override fun compress(): CharArray {
        val list = ArrayList<Char>()
        var cache: Pixel? = null
        var counter = 0
        val dataCopy = ArrayList(data)
        while (dataCopy.size > 0) {
            val temp = dataCopy.removeAt(0)
            if (temp is Pixel) {
                if (cache != null) {
                    list.add(cache.compressWithNext(temp))
                    cache = null
                } else {
                    cache = temp
                }
                counter++
            } else if (temp is Square) {
                if (cache != null) {
                    list.add(cache.compressSingle())
                    list.addAll(temp.dimension.compressDimensionSP())
                } else {
                    list.addAll(temp.dimension.compressDimension())
                }
                cache = temp.pixel
                counter += temp.dimension * temp.dimension
            }
        }
        if (cache != null) {
            list.add(cache.compressSingle())
        }
        return list.toCharArray()
    }

    override fun toString(): String {
        val ret: Array<Array<String?>> = Array(height) { arrayOfNulls<String>(width) }
        val dataCopy = ArrayList(data)
        var row = 0;
        var col = 0;
        while (dataCopy.size > 0) {
            if (col == width) {
                row++
                col = 0
            }
            val temp = dataCopy.removeAt(0)
            if (temp is Pixel) {
                while (ret[row][col] != null) {
                    col++
                    if (col == width) {
                        row++
                        col = 0
                    }
                }
                ret[row][col] = temp.color.toString()
            } else if (temp is Square) {
                for (dRow in 0 until temp.dimension) {
                    for (dCol in 0 until temp.dimension) {
                        ret[row+dRow][col+dCol] = temp.pixel.color.toString()
                    }
                }
            }
            col++
        }
        return ret.joinToString("\n"){ it.joinToString(" ")}
    }


}


private fun ArrayList<Char>.addAll(elements: CharArray) {
    for (element in elements) {
        this.add(element)
    }
}
