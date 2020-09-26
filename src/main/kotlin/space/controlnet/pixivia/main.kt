package space.controlnet.pixivia

import space.controlnet.pixivia.bot.BotContext
import space.controlnet.pixivia.controller.TagModuleController

fun main() {
    val context = BotContext()

    context.run(
        TagModuleController::class
    )
}

