package space.controlnet.pixivia.core.wtdata.api

import space.controlnet.pixivia.exception.ClazzCategoryNotFoundException
import space.controlnet.pixivia.exception.ValueCategoryNotFoundException
import space.controlnet.pixivia.utils.CommandExecutor
import space.controlnet.pixivia.utils.append
import space.controlnet.pixivia.utils.checkOrCreateDir
import java.nio.file.Path
import java.nio.file.Paths

object WTDataVisualizationInterface {
    val rPath: String = "Rscript"
    val rFilePath: Path = Paths.get("src", "main", "resources", "wt", "interface.R").toAbsolutePath()
    val wtDataProjectPath: Path = Paths.get("..", "WT-Data-Project").toAbsolutePath()
    val imageDir: Path = Paths.get("temp").toAbsolutePath()
        .apply(Path::checkOrCreateDir)

    val heatmap: Heatmap = Heatmap

    object Heatmap {
        operator fun invoke(clazz: String, value: String): Path {
            val imagePath: Path = imageDir.append("r_${System.currentTimeMillis()}.png")
            CommandExecutor.exec("$rPath $rFilePath $wtDataProjectPath $imagePath ${ClazzParser(clazz)} ${ValueParser(value)}")
            return imagePath
        }
    }

    object ClazzParser {
        operator fun invoke(clazz: String): String = when (clazz) {
            "空军", "飞机", "空战" -> "aviation"
            "陆军", "坦克", "陆战" -> "ground_vehicles"
            else -> throw ClazzCategoryNotFoundException()
        }
    }

    object ValueParser {
        operator fun invoke(value: String): String = when (value) {
            "场次" -> "battles"
            "胜率" -> "win_rate"
            else -> throw ValueCategoryNotFoundException()
        }
    }
}