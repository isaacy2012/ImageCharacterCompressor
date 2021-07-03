import imageObjects.ImageObject
import imageObjects.Pixel
import imageObjects.Point
import imageObjects.Square

/**
 * Make a compressed Image from an Image
 */
class CompressibleImage(image: Image) : Compressible {
    private var width: Int = 0
    private var height: Int = 0
    //compression squares
    private var data: List<ImageObject> = ArrayList()

    init {
    }

    override fun compress(): CharArray {
        val list = ArrayList<Char>()
        TODO("Not yet fully implemented!")
//        return list.toCharArray
    }


}

private fun ArrayList<Char>.addAll(elements: CharArray) {
    for (element in elements) {
        this.add(element)
    }
}
