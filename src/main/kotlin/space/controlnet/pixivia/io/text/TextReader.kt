package space.controlnet.pixivia.io.text

import space.controlnet.pixivia.io.FileHandler
import java.io.File


class TextReader(override val file: File) : FileHandler {
    fun readAll(): String = file.readText()
}