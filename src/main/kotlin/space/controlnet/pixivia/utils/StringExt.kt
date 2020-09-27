package space.controlnet.pixivia.utils

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.utils.BotConfiguration
import space.controlnet.pixivia.core.tag.localization.ResultParser
import space.controlnet.pixivia.core.tag.prediction.InferenceCommandExecutor
import java.io.File
import java.net.URL

fun String.toBotConfiguration(): BotConfiguration {
    val conf = BotConfiguration()
    conf.fileBasedDeviceInfo(this)
    return conf
}

fun String.asFile(): File = File(this)

fun String.asURL(): URL = URL(this)

fun String.executeInConsole(): String? = InferenceCommandExecutor.exec(this)

fun String.parseResult() = ResultParser.parse(this)
