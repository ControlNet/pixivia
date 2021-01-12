package space.controlnet.pixivia.utils

import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.events.GroupTempMessageEvent
import net.mamoe.mirai.message.data.At
import space.controlnet.pixivia.user.Administrators
import space.controlnet.pixivia.user.BlackList
import java.lang.Exception

suspend fun MessageEvent.replyWithAt(plain: String) = when (this) {
    is GroupMessageEvent -> subject.sendMessage(At(sender) + plain)
    is FriendMessageEvent, is GroupTempMessageEvent -> subject.sendMessage(plain)
    else -> throw Exception("Unexpected message type")
}

suspend fun <T> MessageEvent.checkAdmin(t: T, block: suspend MessageEvent.(T) -> Unit) {
    if (Administrators.contains(sender)) {
        this.block(t)
    } else {
        // if the sender is not an administrator
        replyWithAt("Permission denied")
    }
}

suspend fun <T> MessageEvent.checkBlackList(t: T, block: suspend MessageEvent.(T) -> Unit) {
    if (BlackList.contains(sender)) {
        // if the sender is in blacklist
        replyWithAt("Permission denied")
    } else {
        this.block(t)
    }
}