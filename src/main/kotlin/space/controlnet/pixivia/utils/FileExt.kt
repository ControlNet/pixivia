package space.controlnet.pixivia.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.nio.file.Paths

typealias CsvMap = List<Map<String, String>>

inline fun <reified T : Any> File.readJson(): T {
    return Gson().fromJson(this.reader(), object : TypeToken<T>(){}.type)
}

fun File.readCsv(): CsvMap = csvReader().readAllWithHeader(this)
