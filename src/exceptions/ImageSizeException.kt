package exceptions

import java.lang.RuntimeException


class ImageSizeException(message: String) : RuntimeException(message)