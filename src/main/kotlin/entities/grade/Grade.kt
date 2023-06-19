package entities.grade

import entities.component.Component
import java.util.UUID


/**
 * Represents a Grade, which is a base class for different types of grades associated with components.
 *
 * @param component The component associated with the grade.
 * @param superComponentID The ID of the super component that this grade belongs to.
 * @param id The unique ID of the grade (default value is generated using [UUID.randomUUID()]).
 */
abstract class Grade(
    val component: Component,
    var superComponentID: UUID,
    val id: UUID = UUID.randomUUID()
) {
    //TODO Rename this because it doesn't really make sense...
    internal val subComponents: MutableList<Grade> = mutableListOf()
    var gradeName = component.name

    /**
     * Calculates and returns the grade value for this grade.
     *
     * @return The calculated grade value as a [Double].
     */
    open fun getGrade(): Double {
        var totalPercentage = 0.00
        var grade = 0.0

        subComponents.forEach { subComp ->
            totalPercentage += subComp.component.percentage
        }
         subComponents.forEach {subComp ->
             grade += subComp.getGrade() * (subComp.component.percentage/totalPercentage)
         }
         return String.format("%.1f", grade).toDouble()
    }
}
