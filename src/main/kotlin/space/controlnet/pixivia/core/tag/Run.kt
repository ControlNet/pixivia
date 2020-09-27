package space.controlnet.pixivia.core.tag

import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Image
import space.controlnet.pixivia.core.tag.localization.LocalizationMerger
import space.controlnet.pixivia.user.Administrators
import space.controlnet.pixivia.core.tag.localization.ResultParser
import space.controlnet.pixivia.core.tag.localization.SelfLocalizationSubmitterHandler
import space.controlnet.pixivia.core.tag.prediction.InferenceCommandGenerator
import space.controlnet.pixivia.user.BlackList
import space.controlnet.pixivia.utils.*

val runTagModuleForPrediction: suspend MessageEvent.(String) -> Unit = {
    if (!BlackList.contains(sender)) {
        // filter first image
        val res = message.filterIsInstance<Image>().first().handled()
            // collect url and download the image file
            .withDownloaded()
            .getPath()
            // reply the processing stage to sender
            .apply {
                println("Inference: $this")
                replyWithAt("图片预测中喵~")
            }
            // generate command for tensorflow model
            .let(::InferenceCommandGenerator)
            .generateCommand()
            // execute command, get result of console stdout
            .executeInConsole()
            // apply localization
            ?.parseResult() ?: "找不到匹配的tag喵"

        replyWithAt(res)
    } else {
        replyWithAt("Permission denied")
    }
}


val runTagModuleForLocalizationMerging: suspend FriendMessageEvent.(String) -> Unit = {
    // check if the sender is in admin list
    if (Administrators.contains(sender)) {
        // reload localization file
        LocalizationMerger(ResultParser.getLanguage()).withMerged().save()
        reply("Localization merged")
    } else {
        reply("Permission denied")
    }
}

val runTagModuleForSelfLocalizationSubmission: suspend MessageEvent.(MatchResult) -> Unit = {
    if (!BlackList.contains(sender)) {
        // extract original tag and target tag for translation
        it.groupValues.toTriple()
            // if original tag is existed or not
            .apply {
                if (second in ResultParser.getOriginalTabs()) {
                    // submit the tag
                    let { (_, original, target) ->
                        SelfLocalizationSubmitterHandler.withTags(original, target)
                    }.apply {
                        this(ResultParser.getLanguage())
                        reply("提交成功喵: $second -> $third")
                    }
                } else {
                    reply("没有这个tag喵")
                }
            }
    } else {
        replyWithAt("Permission denied")
    }
}
