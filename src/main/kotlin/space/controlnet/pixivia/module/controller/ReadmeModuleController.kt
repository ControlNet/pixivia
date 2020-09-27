package space.controlnet.pixivia.module.controller

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.readme.runReadmeModule

class ReadmeModuleController(override val bot: Bot) : ModuleController {

    override fun run() {
        bot.subscribeMessages {
            case("Pixivia 使用说明", onEvent = runReadmeModule)
        }
    }
}