package space.controlnet.pixivia.io.download

import io.ktor.client.*
import io.ktor.client.request.*
import space.controlnet.pixivia.utils.with
import java.io.File
import java.net.URL

class ImageDownloader(override val url: URL, override val file: File) : Downloader {

    override suspend fun get(): Unit = HttpClient().with {
        // try to download the image
        println("Downloading: $url")
        val image: ByteArray = this.get(url)
        file.apply(File::createNewFile).writeBytes(image)
    }

}
