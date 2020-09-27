package space.controlnet.pixivia.core.readme

import net.mamoe.mirai.message.MessageEvent
import space.controlnet.pixivia.user.BlackList
import space.controlnet.pixivia.utils.replyWithAt

val runReadmeModule: suspend MessageEvent.(String) -> Unit = {
    if (!BlackList.contains(sender)) {
        replyWithAt(ReadmeLoader.getText())
    } else {
        replyWithAt("Permission denied")
    }
}