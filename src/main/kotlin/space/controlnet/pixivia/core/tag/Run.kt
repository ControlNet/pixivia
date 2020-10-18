package space.controlnet.pixivia.core.tag

import io.ktor.client.features.*
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Image
import space.controlnet.pixivia.core.tag.localization.LocalizationMerger
import space.controlnet.pixivia.core.tag.localization.ResultParser
import space.controlnet.pixivia.core.tag.localization.SelfLocalizationSubmitterHandler
import space.controlnet.pixivia.core.tag.prediction.InferenceCommandGenerator
import space.controlnet.pixivia.utils.*
import java.util.NoSuchElementException

val runTagModuleForPrediction: suspend MessageEvent.(String) -> Unit = { it ->
    checkBlackList(it) {
        // filter first image
        try {
            val res = message.filterIsInstance<Image>()
                .first().handled()
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
                ?.parseResult(n = 10) ?: "找不到匹配的tag喵"
            replyWithAt(res)
        } catch (e: NoSuchElementException) {
            replyWithAt("没有收到色图喵")
        } catch (e: ClientRequestException) {
            replyWithAt("下载失败喵")
        }
    }
}

val runTagModuleForLocalizationMerging: suspend MessageEvent.(String) -> Unit = { it ->
    checkAdmin(it) {
        // reload localization file
        LocalizationMerger(ResultParser.getLanguage())
            .let {
                if (it.hasIncrementalTable()) {
                    it.withMerged().save()
                    replyWithAt("Localization merged")
                } else {
                    replyWithAt("No incremental change")
                }
            }
    }
}

val runTagModuleForSelfLocalizationSubmission: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkBlackList(it) {
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
    }
}
