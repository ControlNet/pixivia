package space.controlnet.pixivia.resources

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import space.controlnet.pixivia.utils.readCsv
import space.controlnet.pixivia.utils.toPair
import java.io.File
import java.nio.file.Paths

class IncrementalLocalizationTable(val language: String): Table {
    // localization csv file location
    override val file: File = Paths
        .get("submission", "$language.csv")
        .toFile()

    fun getMaps(): Map<String, String> = csvReader()
        .readAll(file)
        .map { it.toPair() }
        .toMap()

}