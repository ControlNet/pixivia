package space.controlnet.pixivia.utils

import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.TempMessageEvent
import net.mamoe.mirai.message.data.At
import java.lang.Exception

suspend fun MessageEvent.replyWithAt(text: String) = when (this) {
    is GroupMessageEvent -> reply(At(sender) + text)
    is FriendMessageEvent, is TempMessageEvent -> reply(text)
    else -> throw Exception("Unexpected message type")
}