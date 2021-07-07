package utils.compressors

import imageObjects.Pixel
import imageObjects.Square
import images.CompressibleImage
import images.addAll
import utils.compressDimension
import utils.compressDimensionSP
import java.util.*

fun compressCompressibleImage(cimage: CompressibleImage): CharArray {
    val list = ArrayList<Char>()
//add width and height information
    list.addAll(cimage.width.compressDimensionSP())
    list.addAll(cimage.height.compressDimensionSP())

    var cache: Pixel? = null
    var lastSquare = false
    var counter = 0
    val dataQueue = ArrayDeque(cimage.data)
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
            lastSquare = false
        } else if (temp is Square) {
            // If the cache has a pixel, need to add the cache pixel as a single first
            if (cache != null) {
                list.add(cache.compressSingle())
                cache = null
                // Add this square's dimension as a single pixel-breaking dimension
                list.addAll(temp.dimension.compressDimensionSP())
            } else {
                // if this is the first addition
                if (counter == 0 || lastSquare) {
                    list.addAll(temp.dimension.compressDimensionSP())
                } else {
                    // Add this square's dimension normally
                    list.addAll(temp.dimension.compressDimension())
                }
            }
            cache = temp.pixel
            counter += temp.dimension * temp.dimension
            lastSquare = true
        }
    }
// If there is still a pixel left in the cache, flush it out
    if (cache != null) {
        list.add(cache.compressSingle())
    }

// Return list as CharArray
    return list.toCharArray()
}