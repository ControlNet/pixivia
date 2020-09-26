package space.controlnet.pixivia.downloader

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import space.controlnet.pixivia.utils.append
import java.nio.file.Files
import java.nio.file.Path

class ImageDownloader(directoryPath: Path): Downloader(directoryPath) {

    override fun get(url: String, fileName: String): Path {
        // try to download the image
        println("Downloading: $url")
        val image = runBlocking {
            val client = HttpClient()
            val res: ByteArray = client.get(url)
            client.close()
            res
        }

        // check if the directory path is existed
        if (Files.notExists(directoryPath)) {
            directoryPath.toFile().mkdir()
        }

        // save file
        val imagePath = directoryPath.append("$fileName.jpg")
        val file = imagePath.toFile()
        file.createNewFile()
        file.writeBytes(image)
        return imagePath
    }

}
