package space.controlnet.pixivia.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


inline fun <reified T : Any> File.readJson(): T {
    return Gson().fromJson(this.reader(), object : TypeToken<T>(){}.type)
}

fun File.readCsv(): List<Map<String, String>> = csvReader().readAllWithHeader(this)

fun File.readCsvHeader(): List<String> = csvReader().readAll(this).first()
