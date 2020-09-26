package space.controlnet.pixivia.controller

import net.mamoe.mirai.Bot

abstract class ModuleController(val bot: Bot) {
    abstract fun run()
}