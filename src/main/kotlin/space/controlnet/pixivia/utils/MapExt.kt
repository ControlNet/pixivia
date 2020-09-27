package space.controlnet.pixivia.utils

import space.controlnet.pixivia.exception.KeyNotFoundException

fun <K, V> Map<K, V>.toList(keys: List<K>): List<V> = keys.map { this[it] ?: throw KeyNotFoundException("Key not found") }

