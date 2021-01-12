package space.controlnet.pixivia.module.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.readme.runReadmeModule

class ReadmeModuleController(override val bot: Bot) : ModuleController {

    override fun run() {
        bot.eventChannel.subscribeMessages {
            matching(Regex("^ *Pixivia *使用说明 *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runReadmeModule)

            matching(Regex("^ *Pixivia *-h *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runReadmeModule)

            matching(Regex("^ *Pixivia *--help *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runReadmeModule)
        }
    }
}