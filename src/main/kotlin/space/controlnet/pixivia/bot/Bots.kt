package space.controlnet.pixivia.bot

import net.mamoe.mirai.Bot

object Bots {
    private val objs: List<Pair<Long, Bot>> = BotFactory.createAll()

    fun getFirst(): Bot = objs.first().second
    fun getByQQ(qq: Long): Bot = objs.first { it.first == qq }.second
}