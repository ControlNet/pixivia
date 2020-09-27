package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.io.poster.SelfLocalizationSubmissionPoster

object SelfLocalizationSubmitterHandler {
    fun submit(original: String, target: String, language: String) {
        val data = SelfLocalizationSubmissionData(original, target, language)
        SelfLocalizationSubmissionPoster(data).post()
    }

    fun withTags(original: String, target: String): (String) -> Unit = {language ->
        submit(original, target, language)
    }
}