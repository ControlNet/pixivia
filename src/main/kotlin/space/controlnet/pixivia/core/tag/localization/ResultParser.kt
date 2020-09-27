package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.resources.LocalizationTable
import space.controlnet.pixivia.utils.toTriple

object ResultParser {
    private val translator: Translator = Translator()

    private fun parseComponentsFromRow(row: String): Triple<String, String, String> =
        Regex("(\\(.*\\)) (.+)").find(row)?.groupValues?.toTriple()!!

    private fun translateEachRow(row: String): String? {
        if (row.isBlank()) return null
        val (_, prob, tag) = parseComponentsFromRow(row)
        return "$prob ${translator.translate(tag)}"
    }

    fun getLanguage(): String = translator.language

    val getOriginalTabs: () -> List<String> = LocalizationTable::getOriginalTabs

    fun resetLanguage(language: String): ResultParser {
        translator.language = language
        return this
    }

    fun parse(result: String?): String {
        val lines = result!!.lines()
        return lines.drop(1).mapNotNull(::translateEachRow).joinToString()
    }
}