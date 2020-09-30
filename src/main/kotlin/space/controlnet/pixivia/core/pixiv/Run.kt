package space.controlnet.pixivia.core.pixiv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.message.sendAsImageTo
import net.mamoe.mirai.message.uploadAsImage
import net.mamoe.mirai.utils.ExternalImage
import space.controlnet.pixivia.core.pixiv.api.PixivpyHttpApi
import space.controlnet.pixivia.core.pixiv.entity.PixivImage
import space.controlnet.pixivia.core.pixiv.entity.PixivUser
import space.controlnet.pixivia.data.PixivQQGroupLists
import space.controlnet.pixivia.utils.*
import java.lang.Thread.sleep
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// display one specified image
val runPixivModuleForDisplayingImage: suspend MessageEvent.(MatchResult) -> Unit = { it ->
    checkBlackList(it) {
        it.groupValues.toPair()
            .apply {
                val (_, imageId) = this
                replyWithAt("下载图片中喵: $imageId")
                val (isDownloaded, imagePath) = PixivpyHttpApi.downloadImage(imageId.toLong())
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
        val userId = it.groupValues[1].toLong()
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
        val userId = it.groupValues[1].toLong()
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
            separator = "\n", prefix = "Pixivia正在关注: \n",
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
        // wait for 5 minutes
        delay(interval * 1000)
        println("Finding new images")

        val newImages: List<Triple<PixivImage, Boolean, Path>> = PixivpyHttpApi.getNewImages()
            // only filter the previous 5 minutes
            .filter {
                ChronoUnit.SECONDS.between(it.getCreatedTimeZoned(), previous) <= 0
            }.map {
                // download image
                it.withDownloaded()
            }.filter {
                // only filter the images successfully downloaded
                it.second
            }

        previous = LocalDateTime.now().atZone(ZoneId.systemDefault())

        // for each group
        PixivQQGroupLists.getList()
            .map {
                it.asQQGroup(bot)
            }.forEach { group: Group ->
                // send each image
                newImages.forEach {
                    buildMessageChain {
                        add(it.third.toFile().uploadAsImage(group))
                        add("新色图更新了喵~\n ${it.first.getPixivUrl()}")
                    }.sendTo(group)
                }
            }
    }
}