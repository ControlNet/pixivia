package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.io.poster.SelfLocalizationSubmissionPoster

object SelfLocalizationSubmitter {
    fun submit(original: String, target: String, language: String) {
        val data = SelfLocalizationSubmissionData(original, target, language)
        SelfLocalizationSubmissionPoster(data).post()
    }
}