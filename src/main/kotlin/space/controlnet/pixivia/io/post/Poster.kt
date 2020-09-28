package space.controlnet.pixivia.io.post

import space.controlnet.pixivia.io.FileHandler
import java.io.File

interface Poster<T>: FileHandler {
    val data: T
    override val file: File
    fun post(): Unit
}