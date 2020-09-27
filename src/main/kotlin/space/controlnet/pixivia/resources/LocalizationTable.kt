package space.controlnet.pixivia.resources

import space.controlnet.pixivia.utils.readCsv
import space.controlnet.pixivia.utils.readCsvHeader
import java.io.File
import java.nio.file.Paths

object LocalizationTable: Table {

    // localization csv file location
    private val file: File = Paths
        .get("src", "main", "resources", "localization", "tags.csv")
        .toFile()

    // parse csv file as map
    override var data: List<Map<String, String>> = file.readCsv()

    override val headers: List<String> = file.readCsvHeader()

    fun getOriginalTabs(): List<String> = getColumnByHeader("tag")

    fun selectRowByTag(tag: String): Map<String, String> = data.first { it["tag"] == tag }

    fun reload() {
        data = file.readCsv()
    }
}