package space.controlnet.pixivia.core.pixiv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalImage
import space.controlnet.pixivia.core.pixiv.api.PixivpyHttpApi
import space.controlnet.pixivia.core.pixiv.entity.PixivImage
import space.controlnet.pixivia.core.pixiv.entity.PixivUser
import space.controlnet.pixivia.data.PixivQQGroupLists
import space.controlnet.pixivia.utils.*
import java.lang.Exception
import java.lang.Thread.sleep
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// display one specified image by pixiv id
val runPixivModuleForDisplayingImage: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkBlackList(it) {
        it.groupValues.toPair()
            .apply {
                val (_, imageId) = this
                // try to download the queried image
                replyWithAt("下载图片中喵: $imageId")
                val (isDownloaded, imagePath) = PixivpyHttpApi.downloadImage(imageId.toLong())
                // reply if the image is successfully downloaded
                if (isDownloaded) {
                    imagePath.toFile().sendAsImage()
                } else {
                    replyWithAt("图片下载失败喵")
                }
            }
    }
}

// follow command
val runPixivModuleForFollowingAuthor: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkAdmin(it) {
        // get user pixiv id from regex match result
        val userId = it.groupValues[1].toLong()
        // try to follow the specified user
        if (PixivpyHttpApi.follow(userId)) {
            replyWithAt("关注${userId}成功喵")
        } else {
            replyWithAt("关注${userId}失败喵")
        }
    }
}

// unfollow command
val runPixivModuleForUnfollowingAuthor: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkAdmin(it) {
        // get user pixiv id from regex match result
        val userId = it.groupValues[1].toLong()
        // try to unfollow the specified user
        if (PixivpyHttpApi.unfollow(userId)) {
            replyWithAt("取消关注${userId}成功喵")
        } else {
            replyWithAt("取消关注${userId}失败喵")
        }
    }
}

// display all following authors
val runPixivModuleForDisplayingFollowing: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkBlackList(it) {
        val followingUsers: List<PixivUser> = PixivpyHttpApi.displayFollowing()
        val res = followingUsers.joinToString(
            separator = "\n", prefix = "Pixivia正在关注(总数${followingUsers.size}): \n",
            transform = PixivUser::displayFollowingInfo
        )
        replyWithAt(res)
    }
}

// push new images from following list to all QQ group in list
fun runPixivModuleForPushingNewImages(bot: Bot): suspend CoroutineScope.() -> Unit = {
    val interval = 300L
    var previous = LocalDateTime.now().atZone(ZoneId.systemDefault())
    while (true) {
        println("Finding new images")

        val newImages: List<PixivImage.DownloadedImageEntity> = PixivpyHttpApi.getNewImages()
            .also {
                println("Find ${it.size} images")
            }
            // only filter the previous 5 minutes
            .filter {
                ChronoUnit.SECONDS.between(it.getCreatedTimeZoned(), previous) <= 0
            }.also {
                println("New images: ${it.size}")
            }.map {
                // download image
                it.withDownloaded()
            }.filter {
                // only filter the images successfully downloaded
                it.isDownloaded
            }

        previous = LocalDateTime.now().atZone(ZoneId.systemDefault())

        // for each QQ group in the list
        PixivQQGroupLists.getList()
            .map {
                it.asQQGroup(bot)
            }.forEach { group: Group ->
                // send each image
                newImages.forEach {
                    buildMessageChain {
                        add(it.path.toFile().uploadAsImage(group))
                        add("\n")
                        add("新色图更新了喵~\n 作者: ${it.image.user.name}(${it.image.user.id})\n${it.image.getPixivUrl()}")
                    }.sendTo(group)
                }
            }

        // wait for 5 minutes
        delay(interval * 1000)
    }
}

val runPixivModuleForRecommendation: suspend MessageEvent.(String) -> Unit = {
    checkBlackList(it) {
        PixivpyHttpApi.getRecommendation()
            .map { pixivImage ->
                pixivImage.withDownloaded()
            }.forEach { downloadedEntity ->
                if (downloadedEntity.isDownloaded) {
                    buildMessageChain {
                        add(downloadedEntity.path.toFile().uploadAsImage())
                        add("\n")
                        if (this@checkBlackList is GroupMessageEvent) {
                            add(At(sender))
                        }
                        add(
                            "色图推荐喵~\n 作者: ${downloadedEntity.image.user.name}(${downloadedEntity.image.user.id})\n" +
                                    downloadedEntity.image.getPixivUrl()
                        )
                    }.send()
                }
            }
    }
}