package space.controlnet.pixivia.core.pixiv.entity

data class PixivUser(
    val id: Long,
    val account: String,
    val name: String,
    val is_followed: Boolean,
//    val is_follower: Boolean,
//    val is_friend: Boolean,
//    val is_premium: Boolean?,
//    val profile_image_urls: Map<String, String>,
//    val stats: Map<String, Int>?,
//    val profile: Map<String, String>?
) {
    fun displayFollowingInfo(): String = "$id: $name"
}