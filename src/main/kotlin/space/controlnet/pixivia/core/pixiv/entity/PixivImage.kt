package space.controlnet.pixivia.core.pixiv.entity

import space.controlnet.pixivia.core.pixiv.api.PixivpyHttpApi
import java.nio.file.Path
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class PixivImage(
    val id: Long,
    val title: String,
    val type: String,
    val image_urls: Map<String, String>,
    val caption: String,
    val restrict: Int,
    val user: PixivUser,
    // val tags: List<String>,
    val tools: List<String>,
    val create_date: String,
    val page_count: Int,
    val width: Int,
    val height: Int,
    val sanity_level: String?,
    val x_restrict: Int,
    val series: Any?,
    val meta_single_page: Map<String, String>?,
    val meta_pages: List<Map<String, Map<String, String>>>?,
    val total_view: Long,
    val total_bookmarks: Long,
    val is_bookmarked: Boolean
) {
    fun getCreatedTimeZoned(): ZonedDateTime = create_date.run {
        ZonedDateTime.parse("${substring(0, 10)} ${substring(11, 19)} JST",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"))
    }

    data class DownloadedImageEntity(val image: PixivImage, val isDownloaded: Boolean, val path: Path)

    suspend fun withDownloaded(): DownloadedImageEntity {
        val (isDownloaded, path) = PixivpyHttpApi.downloadImage(id)
        return DownloadedImageEntity(this, isDownloaded, path)
    }

    fun getPixivUrl(): String = "https://www.pixiv.net/artworks/$id"
}


