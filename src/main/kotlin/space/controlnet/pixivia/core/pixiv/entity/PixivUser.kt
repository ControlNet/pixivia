package space.controlnet.pixivia.core.pixiv.entity

data class PixivUser(
    val id: Long,
    val account: String,
    val name: String,
    val is_following: Boolean,
    val is_follower: Boolean,
    val is_friend: Boolean,
    val is_premium: Boolean?,
    val profile_image_urls: Map<String, String>,
    val stats: String?,
    val profile: String?
) {
    fun displayFollowingInfo(): String = "$id: $name"
}