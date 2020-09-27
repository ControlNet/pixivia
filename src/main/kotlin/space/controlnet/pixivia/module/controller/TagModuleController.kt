package space.controlnet.pixivia.module.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.tag.runTagModuleForLocalizationMerging
import space.controlnet.pixivia.core.tag.runTagModuleForPrediction
import space.controlnet.pixivia.core.tag.runTagModuleForSelfLocalizationSubmission

class TagModuleController(override val bot: Bot) : ModuleController {

    override fun run() {
        bot.subscribeMessages {
            contains("这是什么色图", onEvent = runTagModuleForPrediction)
            matching(Regex("^ *翻译 *(.+?) *(?:(?:->)|:) *(.+?) *\$"),
                onEvent = runTagModuleForSelfLocalizationSubmission)
        }

        bot.subscribeFriendMessages {
            case("System call: merge translation", onEvent = runTagModuleForLocalizationMerging)
        }
    }

}

