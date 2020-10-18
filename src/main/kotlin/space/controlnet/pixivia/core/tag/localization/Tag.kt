package space.controlnet.pixivia.core.tag.localization

data class Tag(val prob: String, val tag: String) {
    fun translate(translator: Translator) = Tag(prob, translator.translate(tag))
    override fun toString(): String = "$prob $tag"
}