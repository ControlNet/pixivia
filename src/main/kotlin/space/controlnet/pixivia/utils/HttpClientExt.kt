package space.controlnet.pixivia.utils

import io.ktor.client.*

suspend fun <T> HttpClient.with(block: suspend HttpClient.() -> T): T {
    val res: T = block()
    close()
    return res
}