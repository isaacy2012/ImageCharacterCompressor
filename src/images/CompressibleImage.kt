package images
import Compressible
import imageObjects.ImageObject
import utils.charArrayParser.parseCharArray
import utils.compressors.compressCompressibleImage
import utils.converters.imageToCompressibleImage

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(var width: Int, var height: Int, data: ArrayList<ImageObject>) : Compressible {

    companion object {
        /**
         * Make a CompressibleImage from an Image
         */
        fun of(image: Image): CompressibleImage {
            return imageToCompressibleImage(image)
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
        return compressCompressibleImage(this)
    }

    override fun toString(): String {
        return Image.of(this).toString()
    }

}

fun ArrayList<Char>.addAll(elements: CharArray) {
    for (element in elements) {
        this.add(element)
    }
}
