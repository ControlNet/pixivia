package space.controlnet.pixivia.module

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.module.controller.PixivModuleController

object PixivModule: Module {
    override fun withBot(bot: Bot): PixivModuleController = PixivModuleController(bot)
}