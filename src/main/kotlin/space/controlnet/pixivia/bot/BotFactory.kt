package space.controlnet.pixivia.bot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.BotConfiguration
import space.controlnet.pixivia.resources.BotAccountConfig
import space.controlnet.pixivia.utils.readJson
import space.controlnet.pixivia.utils.toBotConfiguration
import java.nio.file.Paths

object BotFactory {
    private val accountConfigs: List<BotAccountConfig> = Paths
        .get("src", "main", "resources", "config", "bot.json")
        .toFile()
        .readJson()

    private val deviceConfig: BotConfiguration = Paths
        .get("src", "main", "resources", "config", "device.json")
        .toString()
        .toBotConfiguration()

    fun create(accountConfig: BotAccountConfig): Pair<Long, Bot> {
        val (qq, pw) = accountConfig
        return Pair(qq, Bot(qq, pw, deviceConfig))
    }

    fun createAll(): List<Pair<Long, Bot>> = accountConfigs.map(::create)
}