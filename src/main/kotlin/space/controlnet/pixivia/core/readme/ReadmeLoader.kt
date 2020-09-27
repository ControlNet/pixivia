package space.controlnet.pixivia.core.readme

import space.controlnet.pixivia.io.text.TextReader
import java.io.File
import java.nio.file.Paths

object ReadmeLoader {
    val file: File = Paths.get("src", "main", "resources", "config", "readme.txt").toAbsolutePath().toFile()

    fun getText(): String = TextReader(file).readAll()
}