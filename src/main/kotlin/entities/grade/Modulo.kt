package entities.grade

import entities.component.Component
import java.util.*


/**
 * Represents a Modulo, which is a specific type of grade associated with a module.
 *
 * @param moduloName The name of the modulo.
 * @param component The component associated with the modulo grade.
 * @param superComponentID The ID of the super component that this modulo belongs to.
 * @param id The unique ID of the modulo (default value is generated using [UUID.randomUUID()]).
 */
class Modulo(
    val moduloName: String,
    component: Component,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id)