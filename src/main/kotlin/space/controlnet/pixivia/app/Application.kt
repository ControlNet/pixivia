package space.controlnet.pixivia.app;

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import space.controlnet.pixivia.data.warehouse.BotWarehouse
import space.controlnet.pixivia.module.Module


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

            fun withQQ(qq: Long): Application = withBot(BotWarehouse.instance.select.byQQ(qq))

            fun withFirstBot(): Application = withBot(BotWarehouse.instance.select.first())
        }
    }


}
