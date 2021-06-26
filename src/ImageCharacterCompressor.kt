fun main(args: Array<String>) {
    val image = Image("samd.png")
    val sb = StringBuilder()
    for (arr in image.data) {
        for (str in arr) {
            sb.append("$str$str")
        }
        sb.append("\n")
    }

    println(sb.toString())
}