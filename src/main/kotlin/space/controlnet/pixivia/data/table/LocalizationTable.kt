package space.controlnet.pixivia.data.table

import java.io.File
import java.nio.file.Paths

class LocalizationTable: Table() {
    // localization csv file location
    override val file: File = Paths
        .get("src", "main", "resources", "localization", "tags.csv")
        .toFile()

    companion object {
        val instance = LocalizationTable()
    }

    fun getOriginalTabs(): List<String> = select.column.byHeader("tag")

    override val select: Selector = Selector()

    open inner class Selector: Table.Selector() {
        override val row = RowSelector()

        open inner class RowSelector: Table.Selector.RowSelector() {
            fun byTag(tag: String): Map<String, String> = getData().first { it["tag"] == tag }
        }
    }

}