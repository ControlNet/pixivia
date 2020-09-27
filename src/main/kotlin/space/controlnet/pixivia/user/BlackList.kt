package space.controlnet.pixivia.user

import space.controlnet.pixivia.utils.readJson
import java.nio.file.Paths

object BlackList : MemberGroup {
    override fun getList(): List<Long> = Paths
        .get("src", "main", "resources", "config", "blacklist.json")
        .toFile()
        .readJson<List<Long>>()
        .also {
            require(it.size == it.distinct().size) { "Duplicate admin members" }
        }
}

