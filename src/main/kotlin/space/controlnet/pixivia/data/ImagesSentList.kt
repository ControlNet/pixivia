package space.controlnet.pixivia.data

import space.controlnet.pixivia.utils.readJson
import java.io.File
import java.nio.file.Paths

object ImagesSentList {
    val file: File = Paths.get("src", "main", "resources", "config", "pixivQQGroupImageSent.json")
        .toAbsolutePath().toFile()

    fun getList(): List<Long> = file.readJson()
}