package space.controlnet.pixivia.resources

import java.io.File
import java.nio.file.Paths

object LocalizationTable: Table {

    // localization csv file location
    override val file: File = Paths
        .get("src", "main", "resources", "localization", "tags.csv")
        .toFile()

    fun getOriginalTabs(): List<String> = getColumnByHeader("tag")

    fun selectRowByTag(tag: String): Map<String, String> = getData().first { it["tag"] == tag }

}