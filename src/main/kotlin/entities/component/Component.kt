package entities.component

abstract class Component(private val percentage: Int) {
    val subComponent: MutableList<Component> = mutableListOf()

    fun getPercent() = percentage
}