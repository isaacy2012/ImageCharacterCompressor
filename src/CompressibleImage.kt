
import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import utils.charArrayParser.parseCharArray
import utils.compressDimension
import utils.compressDimensionSP
import java.util.*
import kotlin.collections.ArrayList

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(var width: Int, var height: Int, data: ArrayList<ImageObject>) : Compressible {

    companion object {
        /**
         * Make a CompressibleImage from an Image
         */
        fun of(image: Image): CompressibleImage {
            // TODO add logic for detecting and adding squares
            val data = ArrayList<ImageObject>()
            for (row in 0 until image.height) {
                for (col in 0 until image.width) {
                    image.data[row][col]?.let { data.add(it) }
                }
            }
            return CompressibleImage(image.width, image.height, data)
        }

        fun fromCharArray(arr: CharArray): CompressibleImage {
            return parseCharArray(arr)
        }
    }

    // ImageObject
    var data: ArrayList<ImageObject> = ArrayList()

    init {
        this.data = data
    }

    override fun compress(): CharArray {
        val list = ArrayList<Char>()
        //add width and height information
        list.addAll(width.compressDimensionSP())
        list.addAll(height.compressDimensionSP())

        var cache: Pixel? = null
        var counter = 0
        val dataQueue = ArrayDeque(data)
        //pop from queue
        while (dataQueue.isEmpty() == false) {
            val temp = dataQueue.pop()
            if (temp is Pixel) {
                // If there is a pixel already in the cache, pair with this one and add
                if (cache != null) {
                    list.add(cache.compressWithNext(temp))
                    // reset cache
                    cache = null
                } else {
                    // Otherwise set the cache to this pixel
                    cache = temp
                }
                counter++
            } else if (temp is Square) {
                // If the cache has a pixel, we can add that along with the squares's pixel
                if (cache != null) {
                    list.add(cache.compressWithNext(temp.pixel))
                    cache = null
                    // Add this square's dimension as a single pixel-breaking dimension
                    list.addAll(temp.dimension.compressDimensionSP())
                } else {
                    // if this is the first addition
                    if (counter == 0) {
                        list.addAll(temp.dimension.compressDimensionSP())
                    } else {
                        // Add this square's dimension normally
                        list.addAll(temp.dimension.compressDimension())
                    }
                    cache = temp.pixel
                }
                counter += temp.dimension * temp.dimension
            }
        }
        // If there is still a pixel left in the cache, flush it out
        if (cache != null) {
            list.add(cache.compressSingle())
        }

        // Return list as CharArray
        return list.toCharArray()
    }

    override fun toString(): String {
        return Image.of(this).toString()
//        return ret.joinToString("\n"){ it.joinToString(" ")}
    }
}

private fun ArrayList<Char>.addAll(elements: CharArray) {
    for (element in elements) {
        this.add(element)
    }
}
