package space.controlnet.pixivia.core.readme

import net.mamoe.mirai.message.MessageEvent
import space.controlnet.pixivia.user.BlackList
import space.controlnet.pixivia.utils.replyWithAt

val runReadmeModule: suspend MessageEvent.(MatchResult) -> Unit = {
    if (!BlackList.contains(sender)) {
        replyWithAt(ReadmeLoader.getText())
    } else {
        replyWithAt("Permission denied")
    }
}