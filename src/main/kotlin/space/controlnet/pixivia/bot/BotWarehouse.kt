package space.controlnet.pixivia.bot

import net.mamoe.mirai.Bot

object BotWarehouse {
    // create all bots in the beginning
    private val objs: MutableList<Bot> = mutableListOf()
    init {
        Create.all()
    }

    fun getQQList(): List<Long> = objs.map { it.id }

    object Select {
        fun first(): Bot = objs.first()
        fun byQQ(qq: Long): Bot = objs.single { it.id == qq }
    }

    object Drop {
        fun byBot(bot: Bot): BotWarehouse {
            bot.close()
            objs.remove(bot)
            return BotWarehouse
        }

        fun byQQ(qq: Long): BotWarehouse = byBot(Select.byQQ(qq))

        fun all(): BotWarehouse {
            objs.forEach { it.close() }
            objs.clear()
            return BotWarehouse
        }
    }

    object Create {
        fun all(): BotWarehouse {
            // only works if there is no active bots
            require(objs.isEmpty())
            objs.addAll(BotFactory.createAll())
            return BotWarehouse
        }

        fun byQQ(qq: Long): BotFactory {
            // cannot create a duplicate bot for same QQ
            require(qq !in getQQList())
            objs.add(BotFactory.createByQQ(qq))
            return BotFactory
        }
    }

}