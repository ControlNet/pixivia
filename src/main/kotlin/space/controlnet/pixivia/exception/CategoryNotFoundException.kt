package space.controlnet.pixivia.exception

open class CategoryNotFoundException(override val message: String? = null) : Throwable(message)

class ClazzCategoryNotFoundException(override val message: String? = null): CategoryNotFoundException(message)

class ValueCategoryNotFoundException(override val message: String? = null): CategoryNotFoundException(message)