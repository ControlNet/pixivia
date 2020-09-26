package space.controlnet.pixivia.io.poster

import java.io.File

abstract class Poster<T>(val data: T) {
    abstract val file: File
    abstract fun post(): Unit
}