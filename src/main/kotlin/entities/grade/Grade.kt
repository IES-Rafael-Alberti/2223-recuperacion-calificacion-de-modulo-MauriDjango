package entities.grade

import entities.component.Component
import java.util.UUID

abstract class Grade(val component: Component, var superComponentID: UUID, val id: UUID = UUID.randomUUID()) {
    //TODO Rename this because it doesn't really make sense...
    internal val subComponents: MutableList<Grade> = mutableListOf()
    var gradeName = component.componentName

    internal open fun getGrade(): Double {
        var grade = 0.0

         subComponents.forEach {sub ->
             grade += sub.getGrade()*(sub.component.percentage/100)
         }
        return grade
    }
}