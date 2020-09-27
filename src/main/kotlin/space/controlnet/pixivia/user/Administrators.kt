package space.controlnet.pixivia.user

import space.controlnet.pixivia.utils.readJson
import java.nio.file.Paths

object Administrators : MemberGroup {
    override fun getList(): List<Long> = Paths
        .get("src", "main", "resources", "config", "admin.json")
        .toFile()
        .readJson<List<Long>>()
        .also {
            require(it.size == it.distinct().size) { "Duplicate admin members" }
        }
}