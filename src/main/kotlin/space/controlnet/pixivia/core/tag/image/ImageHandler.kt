package space.controlnet.pixivia.core.tag.image

import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import space.controlnet.pixivia.io.download.ImageDownloader
import space.controlnet.pixivia.utils.append
import space.controlnet.pixivia.utils.asURL
import space.controlnet.pixivia.utils.checkOrCreateDir
import java.nio.file.Path
import java.nio.file.Paths

class ImageHandler(val image: Image) {
    companion object {
        private val directoryPath: Path = Paths.get("temp")
            .toAbsolutePath()
            .apply(Path::checkOrCreateDir)
    }

    private suspend fun getUrl(): String = image.queryUrl()

    fun getPath(): Path = directoryPath.append("${image.imageId}.jpg")

    suspend fun withDownloaded(): ImageHandler {
        // apply downloader
        ImageDownloader(getUrl().asURL(), file = getPath().toFile()).get()
        return this
    }

}
