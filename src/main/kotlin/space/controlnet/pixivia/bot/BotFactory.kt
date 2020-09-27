package space.controlnet.pixivia.bot

import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.BotConfiguration
import space.controlnet.pixivia.resources.BotAccountConfig

object BotFactory {

    internal val create: (BotAccountConfig, BotConfiguration) -> Bot = { accountConfig, deviceConfig ->
        val (qq, pw) = accountConfig
        Bot(qq, pw, deviceConfig)
    }

    private val withAccountConfig: (BotAccountConfig) -> (BotConfiguration) -> Bot = { accountConfig: BotAccountConfig ->
        { deviceConfig: BotConfiguration ->
            create(accountConfig, deviceConfig)
        }
    }

    internal val createAll: () -> List<Bot> = {
        val deviceConfig: BotConfiguration = BotContext.readDeviceConfig()
        BotContext.readAccountConfigs()
            .map(withAccountConfig)
            .map { it(deviceConfig) }
    }

    internal val createByQQ: (Long) -> Bot = {qq ->
        val deviceConfig: BotConfiguration = BotContext.readDeviceConfig()
        BotContext.readAccountConfigs()
            .first { it.qq == qq }
            .let { withAccountConfig(it) }
            .let { it(deviceConfig) }
    }
}