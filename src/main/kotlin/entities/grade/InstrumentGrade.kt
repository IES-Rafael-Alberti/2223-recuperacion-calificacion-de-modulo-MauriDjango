package entities.grade

import entities.component.Component
import java.util.*


/**
 * Represents an Instrument Grade, which is a type of grade associated with an Instrument Component.
 *
 * @param component The Instrument component associated with the grade.
 * @param grade The grade value.
 * @param superComponentID The ID of the super component that this grade belongs to.
 * @param id The unique ID of the grade (default value is generated using [UUID.randomUUID()]).
 */
class InstrumentGrade(
    component: Component,
    private var grade: Double,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id) {

    /**
     * Retrieves the grade value, rounded to two decimal places.
     *
     * @return The rounded grade value.
     */
    override fun getGrade(): Double {
        return String.format("%.2f", grade).toDouble()
    }

    /**
     * Sets a new grade value for the instrument.
     *
     * @param newGrade The new grade value to be set.
     */
    fun setGrade(newGrade: Double) {
        grade = newGrade
    }
}
