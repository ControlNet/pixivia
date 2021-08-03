package space.controlnet.pixivia.core.tag.prediction

import java.nio.file.Path
import java.nio.file.Paths

class InferenceCommandGenerator(private val imagePath: Path) {
    private val pythonPath: () -> Path = {
        Paths.get("C:", "Users", "controlnet", "anaconda3", "envs", "pixivia", "python.exe")
            .toAbsolutePath()
    }
    private val scriptPath: () -> Path = {
        Paths.get("src", "main", "resources", "deepdanbooru-master", "inference.py")
            .toAbsolutePath()
    }

    fun generateCommand(): String {
        return "${pythonPath()} ${scriptPath()} \"${imagePath.toAbsolutePath()}\""
    }
}