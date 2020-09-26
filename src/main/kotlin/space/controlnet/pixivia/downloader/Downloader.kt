package space.controlnet.pixivia.downloader

import java.net.URL
import java.nio.file.Path

abstract class Downloader(val directoryPath: Path) {
    abstract fun get(url: String, fileName: String): Path
    fun get(url: URL, fileName: String): Path = get(url.toString(), fileName)
}

