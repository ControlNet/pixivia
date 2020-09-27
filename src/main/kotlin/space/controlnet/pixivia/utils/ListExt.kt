package space.controlnet.pixivia.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

fun <T> List<T>.toTriple(): Triple<T, T, T> {
    return Triple(this[0], this[1], this[2])
}

fun <T> List<T>.toPair(): Pair<T, T> {
    return Pair(this[0], this[1])
}