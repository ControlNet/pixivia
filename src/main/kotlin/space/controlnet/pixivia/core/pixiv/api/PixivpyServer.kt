package space.controlnet.pixivia.core.pixiv.api

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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

    const val url: String = "http://127.0.0.1:5000"

    private lateinit var process: Process

    fun run() {
        val command = "$pythonPath ${scriptPath()} ${getPixivAccount().username} ${getPixivAccount().password} ${getPixivAccount().id} ${tokenDir()} ${imageSavingDir()}"
        println(command)
        process = Runtime.getRuntime().exec(command)
    }

    fun reboot() {
        // stop server and restart
        process.destroy()
        run()
        println("Rebooting pixivpy server!")
    }
}
