package space.controlnet.pixivia.utils

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group

fun Long.asQQFriend(bot: Bot): Friend = bot.getFriend(this)

fun Long.asQQGroup(bot: Bot): Group = bot.getGroup(this)