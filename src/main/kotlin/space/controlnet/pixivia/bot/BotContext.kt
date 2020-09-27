package space.controlnet.pixivia.bot;

import net.mamoe.mirai.utils.BotConfiguration
import space.controlnet.pixivia.resources.BotAccountConfig
import space.controlnet.pixivia.utils.readJson
import space.controlnet.pixivia.utils.toBotConfiguration
import java.nio.file.Paths

object BotContext {
    val readAccountConfigs: () -> List<BotAccountConfig> = {
        Paths
            .get("src", "main", "resources", "config", "bot.json")
            .toAbsolutePath()
            .toFile()
            .readJson<List<BotAccountConfig>>()
            .also {
                // check duplicate QQ
                val qqList = it.map{each -> each.qq}
                if (qqList.size != qqList.distinct().size)
                    throw IllegalStateException("The bot.json has duplicate QQ accounts.")
            }
    }

    val readDeviceConfig: () -> BotConfiguration = {
        Paths
            .get("src", "main", "resources", "config", "device.json")
            .toAbsolutePath()
            .toString()
            .toBotConfiguration()
    }
}
