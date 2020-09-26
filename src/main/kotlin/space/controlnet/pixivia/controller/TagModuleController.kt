package space.controlnet.pixivia.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.tag.runTagModuleForPrediction
import space.controlnet.pixivia.core.tag.runTagModuleForResetLocalization

class TagModuleController(bot: Bot) : ModuleController(bot) {
    override fun run() {
        bot.subscribeMessages {
            contains("这是什么色图", onEvent = runTagModuleForPrediction)
        }

        bot.subscribeFriendMessages {
            case("System call: reload l10n", onEvent = runTagModuleForResetLocalization)
        }
    }

}

