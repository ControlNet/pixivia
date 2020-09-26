package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.utils.CsvMap
import space.controlnet.pixivia.utils.readCsv
import space.controlnet.pixivia.utils.toTriple
import java.io.File
import java.nio.file.Paths

object ResultParser {
    private const val language: String = "sc"

    private val tabMappingFile: File = Paths
        .get("src", "main", "resources", "localization", "tags.csv")
        .toFile()
    private var tabMaps: CsvMap = tabMappingFile.readCsv()

    private fun translate(tag: String): String {
        val translated: String? = tabMaps.first {
            it["tag"] == tag
        }[language]
        return if (translated == "") tag else translated!!
    }

    private fun parseComponentsFromRow(row: String): Triple<String, String, String> =
        Regex("(\\(.*\\)) (.+)").find(row)?.groupValues?.toTriple()!!

    private fun translateEachRow(row: String): String {
        if (row in listOf("", " ")) return ""
        val (text, prob, tag) = parseComponentsFromRow(row)
        return "$prob ${translate(tag)}"
    }

    fun apply(result: String?): String {
        val lines = result!!.lines()
        return lines.subList(fromIndex = 1, toIndex = lines.size).joinToString(transform = ::translateEachRow)
    }

    fun reload() {
        // re-read the csv file
        tabMaps = tabMappingFile.readCsv()
    }
}