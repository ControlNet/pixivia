package space.controlnet.pixivia.core.tag.prediction

import java.nio.file.Path
import java.nio.file.Paths

object InferenceCommand {
    private val pythonPath: Path = Paths.get("python")
    private val scriptPath: Path = Paths.get("src", "main", "resources",
        "deepdanbooru-master" ,"inference.py")

    fun get(imagePath: Path): String {
        return "$pythonPath $scriptPath $imagePath"
    }
}