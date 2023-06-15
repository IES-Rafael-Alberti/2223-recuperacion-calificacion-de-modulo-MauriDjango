package entities.grade

import entities.component.Component
import java.util.*


class CEGrade(
    component: Component,
    superComponentID: UUID,
    id: UUID = UUID.randomUUID()
) : Grade(component, superComponentID, id)


