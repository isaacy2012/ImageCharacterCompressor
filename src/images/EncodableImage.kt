package images
import Encodable
import imageObjects.ImageObject
import utils.charArrayParser.parseCharArray
import utils.compressors.encodeEncodableImage
import utils.converters.imageToEncodableImage

/**
 * Make a compressed Image from an Image
 */
class EncodableImage(var width: Int, var height: Int, data: ArrayList<ImageObject>) : Encodable {

    companion object {
        /**
         * Make a CompressibleImage from an Image
         */
        fun of(image: Image): EncodableImage {
            return imageToEncodableImage(image)
        }

        fun fromCharArray(arr: CharArray): EncodableImage {
            return parseCharArray(arr)
        }
    }

    // ImageObject
    var data: ArrayList<ImageObject> = ArrayList()

    init {
        this.data = data
    }

    override fun encode(): CharArray {
        return encodeEncodableImage(this)
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
