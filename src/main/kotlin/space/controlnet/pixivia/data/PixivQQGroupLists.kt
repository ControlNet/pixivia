package space.controlnet.pixivia.data

import space.controlnet.pixivia.utils.readJson
import java.io.File
import java.nio.file.Paths

object PixivQQGroupLists {
    val file: File = Paths.get("src", "main", "resources", "config", "pixivQQGroup.json")
        .toAbsolutePath().toFile()

    fun getList() = file.readJson<List<Long>>()
}