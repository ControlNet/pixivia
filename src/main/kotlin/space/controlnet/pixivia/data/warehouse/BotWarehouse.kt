package space.controlnet.pixivia.data.warehouse

import net.mamoe.mirai.Bot
import space.controlnet.pixivia.bot.BotFactory

class BotWarehouse: Warehouse<Bot>() {
    companion object {
        val instance = BotWarehouse()
    }

    // create all bots in the beginning
    override val select: Select = Select()
    override val drop: Drop = Drop()
    override val create: Create = Create()

    init {
        create.all()
    }

    fun getQQList(): List<Long> = objs.map { it.id }

    open inner class Select: Warehouse<Bot>.Select() {
        fun byQQ(qq: Long): Bot = objs.single { it.id == qq }
    }

    open inner class Drop: Warehouse<Bot>.Drop() {
        override fun byElement(element: Bot): BotWarehouse {
            element.close()
            objs.remove(element)
            return this@BotWarehouse
        }

        fun byQQ(qq: Long): BotWarehouse = byElement(select.byQQ(qq))

        override fun all(): BotWarehouse {
            objs.forEach { it.close() }
            objs.clear()
            return this@BotWarehouse
        }
    }

    open inner class Create: Warehouse<Bot>.Create() {
        fun all(): BotWarehouse {
            // only works if there is no active bots
            require(objs.isEmpty())
            objs.addAll(BotFactory.createAll())
            return this@BotWarehouse
        }

        fun byQQ(qq: Long): BotWarehouse {
            // cannot create a duplicate bot for same QQ
            require(qq !in getQQList())
            objs.add(BotFactory.createByQQ(qq))
            return this@BotWarehouse
        }
    }

}