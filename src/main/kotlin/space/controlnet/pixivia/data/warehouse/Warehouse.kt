package space.controlnet.pixivia.data.warehouse

abstract class Warehouse<T> {
    protected val objs: MutableList<T> = mutableListOf()

    open val select = Selector()
    open val drop = Dropper()
    open val create = Creator()

    open inner class Selector {
        open fun byIndex(i: Int): T = objs[i]
        open fun first(): T = objs.first()
    }

    open inner class Dropper {
        open fun byElement(element: T): Warehouse<T> {
            objs.remove(element)
            return this@Warehouse
        }

        open fun byIndex(i: Int): Warehouse<T> {
            objs.removeAt(i)
            return this@Warehouse
        }

        open fun all(): Warehouse<T> {
            objs.clear()
            return this@Warehouse
        }
    }

    open inner class Creator
}

