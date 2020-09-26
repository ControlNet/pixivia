package space.controlnet.pixivia.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import java.io.File

typealias CsvMap = List<Map<String, String>>

inline fun <reified T> File.readJson(): T {
    val gson = Gson()
    return gson.fromJson(this.reader(), T::class.java)
}

fun File.readCsv(): CsvMap = csvReader().readAllWithHeader(this)


