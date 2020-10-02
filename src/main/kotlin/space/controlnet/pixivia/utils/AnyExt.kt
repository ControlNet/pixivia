package space.controlnet.pixivia.utils

import com.google.gson.Gson

fun Any.toJson(): String? {
    val gson = Gson()
    return gson.toJson(this)
}