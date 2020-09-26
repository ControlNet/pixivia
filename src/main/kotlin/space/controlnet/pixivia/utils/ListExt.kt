package space.controlnet.pixivia.utils

fun <T> List<T>.toTriple(): Triple<T, T, T> {
    return Triple(this[0], this[1], this[2])
}