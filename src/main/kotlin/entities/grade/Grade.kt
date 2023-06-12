package entities.grade

import entities.component.Component

abstract class Grade(private val component: Component) {
    //TODO Rename this because it doesn't really make sense...
    private val subComponents: MutableList<Grade> = mutableListOf()

    fun getGrade(): Double {
        var grade = 0.0
         subComponents.forEach {sub ->
             grade += sub.getGrade()*(sub.getPercent()/100)
         }
        return grade
    }

    private fun getPercent() = component.getPercent()
}