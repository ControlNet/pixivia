package space.controlnet.pixivia.core.tag

import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import space.controlnet.pixivia.controller.admin.AdminAccounts
import space.controlnet.pixivia.core.tag.localization.ResultParser
import space.controlnet.pixivia.core.tag.prediction.CommandExecutor
import space.controlnet.pixivia.core.tag.prediction.InferenceCommand
import space.controlnet.pixivia.downloader.ImageDownloader
import space.controlnet.pixivia.utils.replyWithAt
import java.nio.file.Path
import java.nio.file.Paths

val runTagModuleForPrediction: suspend MessageEvent.(String) -> Unit = {
    // filter first image
    val image = this.message.filterIsInstance<Image>()[0]
    // download image and get image path
    val path: Path = ImageDownloader(Paths.get("temp")).get(image.queryUrl(), image.imageId)
    println("Inference: $path")
    replyWithAt("图片预测中喵~")
    // generate command for tensorflow model
    val command = InferenceCommand.get(path)
    // execute command, get result of console stdout
    val stdout = CommandExecutor.exec(command)
    // apply localization
    val res = ResultParser.apply(stdout)
    replyWithAt(res)
}


val runTagModuleForResetLocalization: suspend FriendMessageEvent.(String) -> Unit = {
    // check if the sender is in admin list
    if (AdminAccounts.contains(sender)) {
        ResultParser.reload()
        reply("L10n reloaded")
    } else {
        reply("Permission denied")
    }
}

