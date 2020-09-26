package space.controlnet.pixivia.io.poster

import space.controlnet.pixivia.core.tag.localization.SelfLocalizationSubmissionData
import space.controlnet.pixivia.utils.checkOrCreateDir
import java.io.File
import java.nio.file.Paths

class SelfLocalizationSubmissionPoster(
    data: SelfLocalizationSubmissionData
): Poster<SelfLocalizationSubmissionData>(data) {

    override val file: File = Paths.get("submission", "${data.language}.csv").toFile()

    override fun post() {
        // check directory
        Paths.get("submission").checkOrCreateDir()

        // if first time, create a blank csv file
        if (!file.exists()) {
            file.createNewFile()
            file.appendText("original,target\n")
        }

        file.appendText("${data.original},${data.target}\n")
    }

}