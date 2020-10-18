package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.data.table.LocalizationTable
import space.controlnet.pixivia.utils.toTriple

object ResultParser {
    private val translator: Translator = Translator()

    private fun parseComponentsFromRow(row: String): Triple<String, String, String> =
        Regex("(\\(.*\\)) (.+)").find(row)?.groupValues?.toTriple()!!

    private fun translateEachRow(row: String): Tag? {
        if (row.isBlank()) return null
        val (_, prob, tag) = parseComponentsFromRow(row)
        return Tag(prob, tag).translate(translator)
    }

    fun getLanguage(): String = translator.language

    val getOriginalTabs: () -> List<String> = LocalizationTable()::getOriginalTabs

    fun resetLanguage(language: String): ResultParser {
        translator.language = language
        return this
    }

    fun parse(result: String?, n: Int = -1): String {
        val lines = result!!.lines()
        return lines.asSequence().drop(1)
            .mapNotNull(::translateEachRow)
            .sortedByDescending { it.prob }
            .run {
                if (n <= 0) this else take(n)
            }
            .joinToString()
    }
}