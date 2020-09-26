package space.controlnet.pixivia.core.tag

import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.queryUrl
import space.controlnet.pixivia.controller.admin.AdminAccounts
import space.controlnet.pixivia.core.tag.localization.ResultParser
import space.controlnet.pixivia.core.tag.localization.SelfLocalizationSubmitter
import space.controlnet.pixivia.core.tag.prediction.CommandExecutor
import space.controlnet.pixivia.core.tag.prediction.InferenceCommand
import space.controlnet.pixivia.io.downloader.ImageDownloader
import space.controlnet.pixivia.io.poster.SelfLocalizationSubmissionPoster
import space.controlnet.pixivia.utils.replyWithAt
import space.controlnet.pixivia.utils.toTriple
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
    val res = ResultParser.parse(stdout)
    replyWithAt(res)
}


val runTagModuleForLocalizationReset: suspend FriendMessageEvent.(String) -> Unit = {
    // check if the sender is in admin list
    if (AdminAccounts.contains(sender)) {
        // reload localization file
        ResultParser.reload()
        reply("L10n reloaded")
    } else {
        reply("Permission denied")
    }
}

val runTagModuleForSelfLocalizationSubmission: suspend MessageEvent.(MatchResult) -> Unit = {
    val language = ResultParser.getLanguage()
    val (text, original, target) = it.groupValues.toTriple()
    // if original tag is existed or not
    if (original in ResultParser.getOriginalTabs()) {
        SelfLocalizationSubmitter.submit(original, target, language)
        reply("提交成功喵: $original -> $target")
    } else {
        reply("没有这个tag喵")
    }
}