package space.controlnet.pixivia.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.mamoe.mirai.utils.BotConfiguration
import space.controlnet.pixivia.core.tag.localization.ResultParser
import java.io.File
import java.net.URL

fun String.toBotConfiguration(): BotConfiguration {
    val conf = BotConfiguration()
    if (!this.isBlank())
        conf.fileBasedDeviceInfo(this)
    else
        conf.randomDeviceInfo()
    return conf
}

fun String.asFile(): File = File(this)

fun String.asURL(): URL = URL(this)

fun String.executeInConsole(): String? = CommandExecutor.exec(this)

fun String.parseResult(n: Int) = ResultParser.parse(this, n)

inline fun <reified T : Any> String.parseJson(): T {
    return Gson().fromJson(this, object : TypeToken<T>(){}.type)
}