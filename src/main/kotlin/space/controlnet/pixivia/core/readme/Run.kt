package space.controlnet.pixivia.core.readme

import net.mamoe.mirai.event.events.MessageEvent
import space.controlnet.pixivia.utils.checkBlackList
import space.controlnet.pixivia.utils.replyWithAt

val runReadmeModule: suspend MessageEvent.(MatchResult) -> Unit = {it ->
    checkBlackList(it) {
        replyWithAt(ReadmeLoader.getText())
    }
}