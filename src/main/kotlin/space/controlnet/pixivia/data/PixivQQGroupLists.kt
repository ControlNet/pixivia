package space.controlnet.pixivia.data

import com.google.gson.Gson
import space.controlnet.pixivia.utils.readJson
import space.controlnet.pixivia.utils.toJson
import java.io.File
import java.nio.file.Paths

object PixivQQGroupLists {
    val file: File = Paths.get("src", "main", "resources", "config", "pixivQQGroup.json")
        .toAbsolutePath().toFile()

    fun getList(): List<Long> = file.readJson()
}