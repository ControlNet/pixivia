package space.controlnet.pixivia.core.pixiv.entity

import space.controlnet.pixivia.core.pixiv.api.PixivpyHttpApi
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class PixivImage(
    val id: Long,
    val title: String,
    val caption: String,
    val tags: List<String>,
    val tools: List<String>,
    val imageUrl: Map<String, String>,
    val width: Int,
    val height: Int,
    // val stats: Map<String, Map<String, String>>,
    val publicity: Int,
    val age_limit: String,
    val created_time: String,
    val reuploaded_time: String,
    val user: PixivUser,
    val is_manga: Boolean,
    val is_liked: Boolean,
    val favorite_id: Int,
    val page_count: Int,
    val book_style: String,
    val type: String,
    val metadata: String?,
    val constantType: String?,
    val sanityLevel: String
) {
    fun getCreatedTimeZoned(): ZonedDateTime = created_time.let {
        ZonedDateTime.parse("$it JST", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
    }

    fun getReuploadedTimeZoned(): ZonedDateTime = reuploaded_time.let {
        ZonedDateTime.parse("$it JST", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
    }

    data class DownloadedImageEntity(val image: PixivImage, val isDownloaded: Boolean, val path: Path)

    suspend fun withDownloaded(): DownloadedImageEntity {
        val (isDownloaded, path) = PixivpyHttpApi.downloadImage(id)
        return DownloadedImageEntity(this, isDownloaded, path)
    }

    fun getPixivUrl(): String = "https://www.pixiv.net/artworks/$id"
}


