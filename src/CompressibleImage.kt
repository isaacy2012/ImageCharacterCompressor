
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import utils.compressDimension

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(private var width: Int, private var height: Int, data: ArrayList<ImageObject>) : Compressible {
    //compression squares
    var data: ArrayList<ImageObject> = ArrayList()

    init {
        this.data = data
    }

    override fun compress(): CharArray {
        val list = ArrayList<Char>()
        var cache: Pixel? = null
        var counter = 0
        while (data.size > 0) {
            val temp = data.removeAt(0)
            if (temp is Pixel) {
                if (cache != null) {
                    list.add(cache.compressWithNext(temp))
                    cache = null
                } else {
                    cache = temp
                }
                counter++
            } else if (temp is Square) {
                list.addAll(temp.dimension.compressDimension())
                cache = temp.pixel
                counter += temp.dimension * temp.dimension
            }
        }
        if (cache != null) {
            list.addAll(cache.compressSingle())
        }
        return list.toCharArray()
    }


}


private fun ArrayList<Char>.addAll(elements: CharArray) {
    for (element in elements) {
        this.add(element)
    }
}
