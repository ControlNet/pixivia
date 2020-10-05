package space.controlnet.pixivia.module

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.module.controller.WTDataModuleController

object WTDataModule: Module {
    override fun withBot(bot: Bot): WTDataModuleController = WTDataModuleController(bot)
}