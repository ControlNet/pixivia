package space.controlnet.pixivia.module.controller

import net.mamoe.mirai.Bot

interface ModuleController {
    val bot: Bot
    fun run()
}