package space.controlnet.pixivia.data

import space.controlnet.pixivia.utils.readJson
import space.controlnet.pixivia.utils.toJson
import java.io.File
import java.nio.file.Paths

object ImagesSentList {
    val file: File = Paths.get("src", "main", "resources", "config", "pixivQQGroupImageSent.json")
        .toAbsolutePath().toFile()

    fun getList(): List<Long> = file.readJson()

    fun writeList(list: List<Long>) {
        file.writeText(list.toJson() ?: "[]")
    }

    fun appendList(newList: List<Long>) = getList()
        .toMutableList()
        .apply {
            addAll(newList)
        }.let {
            writeList(it)
        }
}
