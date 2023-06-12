package entities.component

abstract class Component(val name: String, private val percentage: Double) {
    val subComponent: MutableList<Component> = mutableListOf()

    fun getPercent() = percentage
}