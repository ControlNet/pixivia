package space.controlnet.pixivia.core.wtdata

import kotlinx.coroutines.TimeoutCancellationException
import net.mamoe.mirai.message.MessageEvent
import space.controlnet.pixivia.core.wtdata.api.WTDataVisualizationInterface
import space.controlnet.pixivia.exception.ClazzCategoryNotFoundException
import space.controlnet.pixivia.exception.ValueCategoryNotFoundException
import space.controlnet.pixivia.utils.checkBlackList
import space.controlnet.pixivia.utils.replyWithAt
import space.controlnet.pixivia.utils.toTriple
import java.nio.file.Path

val runWTDataModuleForPlottingHeatmap: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkBlackList(it) {
        it.groupValues.toTriple().also { (_, clazz, value) ->
            try {
                val imagePath: Path = WTDataVisualizationInterface.heatmap(clazz, value)
                try {
                    imagePath.toFile().sendAsImage()
                } catch (e: TimeoutCancellationException) {
                    imagePath.toFile().sendAsImage()
                }
            } catch (e: ClazzCategoryNotFoundException) {
                replyWithAt("不知道什么是\"$clazz\"喵")
            } catch (e: ValueCategoryNotFoundException) {
                replyWithAt("不知道什么是\"$value\"喵")
            }
        }
    }
}