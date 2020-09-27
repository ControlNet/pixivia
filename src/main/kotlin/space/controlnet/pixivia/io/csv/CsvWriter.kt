package space.controlnet.pixivia.io.csv

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import space.controlnet.pixivia.utils.toList
import java.io.File

class CsvWriter(val file: File) {

    fun saveAsCsv(data: List<List<String>>) {
        if (!file.exists()) file.createNewFile()
        csvWriter().open(file) {
            this.writeAll(data)
        }
    }

    fun saveAsCsv(data: List<Map<String, String>>, headers: List<String>) {
        if (!file.exists()) file.createNewFile()
        val body: Array<List<String>> = data.map { it.toList(keys = headers) }.toTypedArray()
        saveAsCsv(listOf(headers, *body))
    }

}