package space.controlnet.pixivia.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.tag.runTagModuleForPrediction
import space.controlnet.pixivia.core.tag.runTagModuleForLocalizationReset
import space.controlnet.pixivia.core.tag.runTagModuleForSelfLocalizationSubmission

class TagModuleController(bot: Bot) : ModuleController(bot) {
    override fun run() {
        bot.subscribeMessages {
            contains("这是什么色图", onEvent = runTagModuleForPrediction)
            matching(Regex("^ *翻译 *(.+?) *(?:(?:->)|:) *(.+?) *\$"),
                onEvent = runTagModuleForSelfLocalizationSubmission)
        }

        bot.subscribeFriendMessages {
            case("System call: reload l10n", onEvent = runTagModuleForLocalizationReset)
        }
    }

}

