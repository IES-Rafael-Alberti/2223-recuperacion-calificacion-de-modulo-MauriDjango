package entities.grade

import entities.component.Component
import java.util.*

class InstrumentGrade(
    component: Component,
    private val grade: Double,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id) {
    override fun getGrade(): Double = grade
}
