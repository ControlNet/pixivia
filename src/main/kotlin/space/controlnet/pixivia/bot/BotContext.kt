package space.controlnet.pixivia.bot

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import space.controlnet.pixivia.controller.ModuleController
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class BotContext(val bot: Bot) {

    constructor(): this(Bots.getFirst())
    constructor(qq: Long): this(Bots.getByQQ(qq))


    fun run(vararg modules: KClass<out ModuleController>) {
        runBlocking {
            bot.alsoLogin()
            for (module in modules) {
                module.primaryConstructor!!.call(bot).run()
            }
            bot.join()
        }
    }
}