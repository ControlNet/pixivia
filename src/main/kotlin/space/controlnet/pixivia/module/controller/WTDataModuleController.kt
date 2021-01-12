package space.controlnet.pixivia.module.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.wtdata.runWTDataModuleForPlottingHeatmap

class WTDataModuleController(override val bot: Bot) : ModuleController {
    override fun run() {
        bot.eventChannel.subscribeMessages {
            matching(Regex("^pixivia *WT *(..)(..)图? *\$", option = RegexOption.IGNORE_CASE),
                    onEvent = runWTDataModuleForPlottingHeatmap)
        }
    }

}
