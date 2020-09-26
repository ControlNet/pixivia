package space.controlnet.pixivia.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

typealias CsvMap = List<Map<String, String>>

inline fun <reified T : Any> File.readJson(): T {
    val gson = Gson()
    return gson.fromJson(this.reader(), object : TypeToken<T>(){}.type)
}

fun File.readCsv(): CsvMap = csvReader().readAllWithHeader(this)
