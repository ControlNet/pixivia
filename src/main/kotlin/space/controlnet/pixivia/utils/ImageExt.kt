package space.controlnet.pixivia.utils

import net.mamoe.mirai.message.data.Image
import space.controlnet.pixivia.core.tag.image.ImageHandler

fun Image.handled(): ImageHandler = ImageHandler(this)