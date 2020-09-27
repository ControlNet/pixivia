package space.controlnet.pixivia.app;

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import space.controlnet.pixivia.bot.BotWarehouse
import space.controlnet.pixivia.module.Module
import space.controlnet.pixivia.module.controller.ModuleController
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class Application(private val bot: Bot, private val modules: Array<out Module>) {
    fun runApp() {
        runBlocking {
            bot.alsoLogin()
            for (module in modules) {
                module.withBot(bot).run()
            }
            bot.join()
        }
    }

    object Builder {
        fun registerModules(vararg modules: Module): ApplicationBuilder = ApplicationBuilder { bot: Bot ->
                Application(bot, modules)
            }

        class ApplicationBuilder(val buildApp: (Bot) -> Application) {
            fun withBot(bot: Bot): Application = buildApp(bot)

            fun withQQ(qq: Long): Application = withBot(BotWarehouse.Select.byQQ(qq))

            fun withFirstBot(): Application = withBot(BotWarehouse.Select.first())
        }
    }


}
