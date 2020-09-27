package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.resources.LocalizationTable


internal class Translator(var language: String = "sc") {

    fun translate(tag: String): String {
        val translated: String? = LocalizationTable.selectRowByTag(tag)[language]
        return if (translated == "") tag else translated!!
    }
}