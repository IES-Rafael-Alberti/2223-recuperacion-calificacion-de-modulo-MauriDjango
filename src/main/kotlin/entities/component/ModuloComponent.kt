package entities.component


/**
 * Represents a ModuloComponent, which is a specific type of component associated with a Modulo.
 * Inherits from the [Component] abstract class.
 *
 * @param moduloName The name of the Modulo associated with the ModuloComponent.
 * @param percentage The weightage or percentage of the ModuloComponent (default: 100.00).
 */
class ModuloComponent(moduloName: String, percentage: Double = 100.00) : Component(moduloName, percentage) {
}