package space.controlnet.pixivia.module

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.module.controller.ReadmeModuleController

object ReadmeModule: Module {
    override fun withBot(bot: Bot): ReadmeModuleController = ReadmeModuleController(bot)
}