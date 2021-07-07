import images.EncodableImage
import images.Image

fun main(args: Array<String>) {
    val image = Image("samd.png")
    print(image.toString())
    println("==========================================")
    val smaller = image.resizeToWidth(20)
    val compressible = EncodableImage.of(smaller)
    println(compressible.encode())
}