package space.controlnet.pixivia.core.pixiv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import space.controlnet.pixivia.core.pixiv.api.PixivpyHttpApi
import space.controlnet.pixivia.core.pixiv.api.PixivpyServer
import space.controlnet.pixivia.core.pixiv.entity.PixivUser
import space.controlnet.pixivia.data.ImagesSentList
import space.controlnet.pixivia.data.PixivQQGroupLists
import space.controlnet.pixivia.utils.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

// display one specified image by pixiv id
val runPixivModuleForDisplayingImage: suspend MessageEvent.(MatchResult) -> Unit = { it: MatchResult ->

    checkBlackList(it) {
        it.groupValues.toPair()
            .apply {
                val (_, imageId) = this
                // try to download the queried image
                replyWithAt("下载图片中喵: $imageId")
                val (isDownloaded, imagePath) = PixivpyHttpApi.downloadImage(imageId.toLong())
                // reply if the image is successfully downloaded
                if (isDownloaded) {
                    imagePath.toFile().sendAsImageTo(this@checkBlackList.subject)
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
            replyWithAt("关注成功喵 ${userId}: ${PixivpyHttpApi.getUserInfo(userId).name}")
        } else {
            replyWithAt("关注失败喵 ${userId}: ${PixivpyHttpApi.getUserInfo(userId).name}")
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
        val res = followingUsers
            .sortedBy { it.id.toString() }
            .joinToString(
            separator = "\n", prefix = "Pixivia正在关注(总数${followingUsers.size}): \n",
            transform = PixivUser::displayFollowingInfo
        )
        replyWithAt(res)
    }
}

// push new images from following list to all QQ group in list
fun runPixivModuleForPushingNewImages(bot: Bot): suspend CoroutineScope.() -> Unit = {
    val interval = 1800L
    var previous = LocalDateTime.now().atZone(ZoneId.systemDefault())
    while (true) {
        println("Finding new images")
        try {
            PixivpyHttpApi.getNewImages()
                // only filter the images published in previous 24 hours as buffer
                .filter {
                    ChronoUnit.SECONDS.between(it.getCreatedTimeZoned(), previous) <= 24 * 3600
                }.filter {
                    it.id !in ImagesSentList.getList()
                }.map {
                    // download image
                    it.withDownloaded()
                }.filter {
                    // only filter the images successfully downloaded
                    it.isDownloaded
                }.also {
                    println("New images: ${it.size}")
                }
                // for each new image, send to each QQ group in the list
                .forEach { newImage ->
                    PixivQQGroupLists
                        .getList()
                        .map{ it.asQQGroup(bot) }
                        .forEach { group ->
                            buildMessageChain {
                                add(newImage.path.toFile().uploadAsImage(group))
                                add("\n")
                                add("新色图更新了喵~\n作者: ${newImage.image.user.name}(${newImage.image.user.id})\n${newImage.image.getPixivUrl()}")
                            }.sendTo(group)
                        }
                    // add to the sent list
                    ImagesSentList.append(newImage.image.id)
                }

            // reset timer
            previous = LocalDateTime.now().atZone(ZoneId.systemDefault())

            // wait for next interval
            delay(interval * 1000)

        } catch (e: TimeoutCancellationException) {
            println("Timeout, retrying")
            // PixivpyServer.reboot()
            // wait for 30 seconds
            delay(30 * 1000)
            continue
        }
    }
}

val runPixivModuleForAutoRebootingServer: suspend CoroutineScope.() -> Unit = {
    val interval = 21600L // reboot server every 6 hours
    while (true) {
        delay(interval * 1000)
        PixivpyServer.reboot()
    }
}

val runPixivModuleForManuallyRebootingServer: suspend MessageEvent.(String) -> Unit = {
    checkAdmin(it) {
        PixivpyServer.reboot()
        // wait for 30 seconds
        delay(30 * 1000)
        replyWithAt("Pixivpy server rebooted.")
    }
}


val runPixivModuleForRecommendation: suspend MessageEvent.(MatchResult) -> Unit = {
    checkBlackList(it) {
        PixivpyHttpApi.getRecommendation()
            .map { pixivImage ->
                pixivImage.withDownloaded()
            }.forEach { downloadedEntity ->
                if (downloadedEntity.isDownloaded) {
                    buildMessageChain {
                        add(downloadedEntity.path.toFile().uploadAsImage(this@checkBlackList.subject))
                        add("\n")
                        if (this@checkBlackList is GroupMessageEvent) {
                            add(At(sender))
                        }
                        add(
                            "色图推荐喵~\n 作者: ${downloadedEntity.image.user.name}(${downloadedEntity.image.user.id})\n" +
                                    downloadedEntity.image.getPixivUrl()
                        )
                    }.apply {
                        subject.sendMessage(this)
                    }
                }
            }
    }
}