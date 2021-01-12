package space.controlnet.pixivia.module.controller

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeMessages
import space.controlnet.pixivia.core.pixiv.*
import space.controlnet.pixivia.core.pixiv.api.PixivpyServer

class PixivModuleController(override val bot: Bot) : ModuleController {
    init {
        // run Pixivpy-based server
        // PixivpyServer.run()
    }

    override fun run() {
        bot.eventChannel.subscribeMessages {
            matching(Regex("^ *看图 *?(\\d+) *\$"), onEvent = runPixivModuleForDisplayingImage)
            matching(Regex("^ *Pixivia *关注 *?(\\d+) *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runPixivModuleForFollowingAuthor)
            matching(Regex("^ *Pixivia *取消关注 *?(\\d+) *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runPixivModuleForUnfollowingAuthor)
            matching(Regex("^ *Pixivia *关注列表 *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runPixivModuleForDisplayingFollowing)
            matching(Regex("^ *Pixivia *色图推荐 *\$", option = RegexOption.IGNORE_CASE),
                onEvent = runPixivModuleForRecommendation)
            case("System call: reboot pixivpy server", onEvent = runPixivModuleForManuallyRebootingServer)
        }

        bot.launch(block = runPixivModuleForPushingNewImages(bot))
        // bot.launch(block = runPixivModuleForAutoRebootingServer)
    }


}