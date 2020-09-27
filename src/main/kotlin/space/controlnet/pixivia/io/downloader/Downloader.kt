package space.controlnet.pixivia.io.downloader

import space.controlnet.pixivia.io.FileHandler
import java.io.File
import java.net.URL

interface Downloader: FileHandler {
    val url: URL
    override val file: File

    suspend fun get(): Unit
}

