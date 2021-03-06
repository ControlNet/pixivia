package space.controlnet.pixivia.core.pixiv.api

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.withTimeout
import space.controlnet.pixivia.core.pixiv.entity.PixivImage
import space.controlnet.pixivia.core.pixiv.entity.PixivUser
import space.controlnet.pixivia.utils.parseJson
import space.controlnet.pixivia.utils.with
import java.nio.file.Path
import java.nio.file.Paths

object PixivpyHttpApi {
    suspend fun queryImage(imageId: Long): PixivImage = HttpClient().with<String> {
        get(PixivpyServer.url + "/query/image/$imageId")
    }.parseJson()

    suspend fun downloadImage(imageId: Long): Pair<Boolean, Path> {
        val res: String = HttpClient().with {
            try {
                this.get(PixivpyServer.url + "/download/image/$imageId")
            } catch (e: ServerResponseException) {
                "FALSE"
            }
        }
        val imagePath = Paths.get(PixivpyServer.imageSavingDir().toString(), "$imageId.png")
        return Pair(res == "TRUE", imagePath)
    }

    suspend fun displayFollowing(): List<PixivUser> = HttpClient().with<String> {
        this.get(PixivpyServer.url + "/following/display")
    }.parseJson()

    suspend fun follow(userId: Long): Boolean = HttpClient().with<String> {
        this.get(PixivpyServer.url + "/following/follow/$userId")
    } == "success"

    suspend fun unfollow(userId: Long): Boolean = HttpClient().with<String> {
        this.get(PixivpyServer.url + "/following/unfollow/$userId")
    } == "success"

    suspend fun getNewImages(): List<PixivImage> = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
        }
    }.with<String> {
        this.get(PixivpyServer.url + "/following/new")
    }.parseJson()

    suspend fun getRecommendation(n: Int = 1): List<PixivImage> = HttpClient().with<String> {
        this.get(PixivpyServer.url + "/recommend/image")
    }.parseJson<List<PixivImage>>().take(n)

    suspend fun getUserInfo(userId: Long): PixivUser = HttpClient().with<String> {
        this.get(PixivpyServer.url + "/query/user/$userId")
    }.parseJson()
}
