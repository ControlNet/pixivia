package space.controlnet.pixivia.module

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.module.controller.ModuleController


interface Module {
    fun withBot(bot: Bot): ModuleController
}


