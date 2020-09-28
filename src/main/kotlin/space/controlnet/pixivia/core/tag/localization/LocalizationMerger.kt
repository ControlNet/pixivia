package space.controlnet.pixivia.core.tag.localization

import space.controlnet.pixivia.io.csv.CsvWriter
import space.controlnet.pixivia.data.table.IncrementalLocalizationTable
import space.controlnet.pixivia.data.table.LocalizationTable
import java.io.File
import java.nio.file.Paths

class LocalizationMerger(val language: String) {
    private val oldTable = LocalizationTable.instance
    private val incrementalTable = IncrementalLocalizationTable(language)

    companion object {
        private val file: File = Paths.get("src","main", "resources", "localization","tags.csv")
            .toAbsolutePath().toFile()
        private val backup: File = Paths.get("src","main", "resources", "localization","tags.bak.csv")
            .toAbsolutePath().toFile()
    }

    fun hasIncrementalTable(): Boolean = incrementalTable.file.exists()

    private lateinit var merged: List<Map<String, String>>

    fun withMerged(): LocalizationMerger {
        val newDataOriginalTags = incrementalTable.select.column.byHeader("original")
        val newDataMapping: Map<String, String> = incrementalTable.getMaps()

        return oldTable.getData()
            .toList() // copy
            .map(Map<String, String>::toMutableMap)
            // merge the translation
            .map {
                // if the tag is in new data
                if (it["tag"] in newDataOriginalTags) {
                    it[language] = newDataMapping[it["tag"]]!!
                }
                it
            }.let {
                merged = it
                this
            }
    }

    fun save() {
        // drop old backup
        if (backup.exists()) backup.delete()
        // backup the current data
        val headers = oldTable.getHeaders()
        file.renameTo(backup)
        // generate new data
        CsvWriter(file).saveAsCsv(merged, headers)
        // drop submission data
        incrementalTable.file.delete()
    }
}

