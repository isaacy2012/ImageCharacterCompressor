import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Square
import utils.compressDimension
import utils.compressDimensionSP
import java.util.*
import kotlin.collections.ArrayList

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(private var width: Int, private var height: Int, data: ArrayList<ImageObject>) : Compressible {

    companion object {
        /**
         * Make a CompressibleImage from an Image
         */
        fun of(image: Image): CompressibleImage {
            TODO()
            return CompressibleImage(image.width, image.height, ArrayList())
        }
    }

    // ImageObject
    var data: ArrayList<ImageObject> = ArrayList()

    init {
        this.data = data
    }

    override fun compress(): CharArray {
        val list = ArrayList<Char>()
        var cache: Pixel? = null
        var counter = 0
        val dataQueue = ArrayDeque(data)
        //pop from queue
        while (dataQueue.size > 0) {
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
                // If the cache has a pixel, then it is alone, and needs to be compressed by itself
                if (cache != null) {
                    list.add(cache.compressSingle())
                    // Add this square's dimension as a single pixel-breaking dimension
                    list.addAll(temp.dimension.compressDimensionSP())
                } else {
                    // Add this square's dimension normally
                    list.addAll(temp.dimension.compressDimension())
                }
                cache = temp.pixel
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
        val ret: Array<Array<String?>> = Array(height) { arrayOfNulls<String>(width) }
        var row = 0;
        var col = 0;
        val dataQueue = ArrayDeque(data)
        //pop from queue
        while (dataQueue.size > 0) {
            // wrap around at width
            if (col == width) {
                row++
                col = 0
            }
            val temp = dataQueue.pop()
            //add to the array if single pixel
            if (temp is Pixel) {
                // if current [row][col] has already been filled by a square
                // keep going through the scan until there is an unfilled pixel space
                while (ret[row][col] != null) {
                    col++
                    if (col == width) {
                        row++
                        col = 0
                    }
                }
                ret[row][col] = temp.color.toString()
            } else if (temp is Square) {
                // fill each pixel space in the square
                for (dRow in 0 until temp.dimension) {
                    for (dCol in 0 until temp.dimension) {
                        ret[row+dRow][col+dCol] = temp.pixel.color.toString()
                    }
                }
            }
            //after each, continue scan
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
