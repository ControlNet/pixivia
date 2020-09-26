package space.controlnet.pixivia.core.tag.prediction

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object CommandExecutor {
// From https://stackoverflow.com/questions/35421699/how-to-invoke-external-command-from-within-kotlin-code

    /** Run a system-level command.
     * Note: This is a system independent java exec (e.g. | doesn't work). For shell: prefix with "bash -c"
     * Inputting the string in stdIn (if any), and returning stdout and stderr as a string. */
    fun exec(cmd: String, stdIn: String = "", workingDir: File = File(".")): String? {
        try {
            val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start().apply {
                    if (stdIn != "") {
                        outputStream.bufferedWriter().apply {
                            write(stdIn)
                            flush()
                            close()
                        }
                    }
                    waitFor(60, TimeUnit.SECONDS)
                }
            return process.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}