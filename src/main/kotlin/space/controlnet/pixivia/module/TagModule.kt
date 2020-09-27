package space.controlnet.pixivia.module

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.module.controller.TagModuleController

object TagModule: Module {
    override fun withBot(bot: Bot): TagModuleController = TagModuleController(bot)
}