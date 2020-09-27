package space.controlnet.pixivia.user

import net.mamoe.mirai.contact.User

interface MemberGroup {

    fun getList(): List<Long>

    fun contains(qq: Long): Boolean = qq in getList()

    fun contains(user: User): Boolean = contains(user.id)
}