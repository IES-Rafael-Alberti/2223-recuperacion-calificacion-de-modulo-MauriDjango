package entities.grade

import entities.component.Component
import java.util.*


class Modulo(val moduloName: String, component: Component, superComponentID: UUID, id: UUID = UUID.randomUUID()) : Grade(component, superComponentID, id)