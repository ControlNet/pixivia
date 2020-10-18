package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.data.table.LocalizationTable


class Translator(var language: String = "sc") {

    fun translate(tag: String): String {
        val translated: String? = LocalizationTable.instance.select.row.byTag(tag)[language]
        return if (translated == "") tag else translated!!
    }
}