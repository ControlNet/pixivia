package space.controlnet.pixivia.core.tag.localization

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import space.controlnet.pixivia.utils.CsvMap
import space.controlnet.pixivia.utils.readCsv
import java.io.File
import java.nio.file.Paths

internal class Translator(var language: String = "sc") {
    // localization csv file location
    private val tabMappingFile: File = Paths
        .get("src", "main", "resources", "localization", "tags.csv")
        .toFile()

    // parse csv file as map
    private var tabMaps: CsvMap = tabMappingFile.readCsv()

    val originalTabs = tabMaps.map { it["tag"] }

    fun translate(tag: String): String {
        val translated: String? = tabMaps.first {
            it["tag"] == tag
        }[language]
        return if (translated == "") tag else translated!!
    }

    fun reload() {
        // re-read the csv file
        tabMaps = tabMappingFile.readCsv()
    }
}