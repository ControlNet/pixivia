package space.controlnet.pixivia.core.pixiv.api

import space.controlnet.pixivia.data.PixivAccount
import space.controlnet.pixivia.utils.readJson
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object PixivpyServer {

    private val pixivAccountFile: File = Paths.get("src", "main", "resources", "config", "pixivAcc.json")
        .toAbsolutePath()
        .toFile()

    private fun getPixivAccount(): PixivAccount = pixivAccountFile.readJson()

    private val tokenDir: () -> Path = {
        Paths.get("src", "main", "resources", "config", "pixivToken.json")
            .toAbsolutePath()
    }

    internal val imageSavingDir: () -> Path = {
        Paths.get("temp")
            .toAbsolutePath()
    }

    private val pythonPath: String = "python"

    private val scriptPath: () -> Path = {
        Paths.get("..", "pixivia.pixivpy-server", "app.py")
            .toAbsolutePath()
    }

    val url: String = "http://127.0.0.1:5000"

    fun run() {
        val command = "$pythonPath ${scriptPath()} ${getPixivAccount().username} ${getPixivAccount().password} ${tokenDir()} ${imageSavingDir()}"
        Runtime.getRuntime().exec(command)
    }
}
