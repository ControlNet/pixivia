package space.controlnet.pixivia.controller.admin

import net.mamoe.mirai.contact.User
import space.controlnet.pixivia.utils.readJson
import java.nio.file.Paths

object AdminAccounts {
    private val list: List<Long> = Paths
        .get("src", "main", "resources", "config", "admin.json")
        .toFile()
        .readJson()

    fun contains(qq: Long): Boolean = qq in list

    fun contains(user: User): Boolean = contains(user.id)
}