fun main(args: Array<String>) {
    val image = Image("samd.png")
    print(image.toString())
    println("==========================================")
    val smaller = image.resizeToWidth(20)
    val compressible = CompressibleImage.of(smaller)
    println(compressible.compress())
}