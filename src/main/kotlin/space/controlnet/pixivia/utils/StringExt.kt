package space.controlnet.pixivia.utils

import net.mamoe.mirai.utils.BotConfiguration

fun String.toBotConfiguration(): BotConfiguration {
    val conf = BotConfiguration()
    conf.fileBasedDeviceInfo(this)
    return conf
}