package space.controlnet.pixivia.io.downloader

import java.net.URL
import java.nio.file.Path

interface Downloader {
    fun get(url: String, fileName: String): Path
    fun get(url: URL, fileName: String): Path = get(url.toString(), fileName)
}

