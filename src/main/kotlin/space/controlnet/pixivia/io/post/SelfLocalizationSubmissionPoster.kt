package space.controlnet.pixivia.io.post

import space.controlnet.pixivia.core.tag.localization.SelfLocalizationSubmissionData
import space.controlnet.pixivia.utils.append
import space.controlnet.pixivia.utils.checkOrCreateDir
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class SelfLocalizationSubmissionPoster(override val data: SelfLocalizationSubmissionData): Poster<SelfLocalizationSubmissionData> {

    companion object {
        val directoryPath: Path = Paths.get("submission").toAbsolutePath().apply(Path::checkOrCreateDir)
    }

    override val file: File = directoryPath.append("${data.language}.csv").toFile().apply {
        // check if the file is existed
        if (!exists()) {
            createNewFile()
            appendText("original,target\n")
        }
    }

    override fun post(): Unit {
        file.appendText("${data.original},${data.target}\n")
    }

}