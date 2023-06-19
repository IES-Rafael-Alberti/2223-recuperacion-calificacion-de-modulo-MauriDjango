package entities.grade

import entities.component.Component
import java.util.*


/**
 * Represents a CE Grade, which is a type of grade associated with a CE Component.
 *
 * @param component The CE component associated with the grade.
 * @param superComponentID The ID of the super component that this grade belongs to.
 * @param id The unique ID of the grade (default value is generated using [UUID.randomUUID()]).
 */
class CEGrade(
    component: Component,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id)
