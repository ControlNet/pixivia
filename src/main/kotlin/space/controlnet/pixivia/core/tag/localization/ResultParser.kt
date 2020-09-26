package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.utils.toTriple

object ResultParser {
    private val translator: Translator = Translator()

    private fun parseComponentsFromRow(row: String): Triple<String, String, String> =
        Regex("(\\(.*\\)) (.+)").find(row)?.groupValues?.toTriple()!!

    private fun translateEachRow(row: String): String {
        if (row in listOf("", " ")) return ""
        val (text, prob, tag) = parseComponentsFromRow(row)
        return "$prob ${translator.translate(tag)}"
    }

    fun parse(result: String?): String {
        val lines = result!!.lines()
        return lines.subList(fromIndex = 1, toIndex = lines.size).joinToString(transform = ::translateEachRow)
    }

    fun getLanguage(): String = translator.language

    fun getOriginalTabs(): List<String?> = translator.originalTabs

    fun resetLanguage(language: String): ResultParser {
        translator.language = language
        return this
    }

    fun reload() = translator.reload()
}