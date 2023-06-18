package entities.grade

import entities.component.Component
import java.util.*


/**
 * Represents a grade for a specific Resultado de Aprendizaje (RA).
 *
 * @param component The component associated with the RA grade.
 * @param superComponentID The ID of the super component that this RA grade belongs to.
 * @param id The unique ID of the RA grade (default value is generated using [UUID.randomUUID()]).
 */
class RAGrade(
    component: Component,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id)