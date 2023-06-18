package entities.component


/**
 * Represents an RAComponent, which is a specific type of component associated with a ResultadoAprendizaje.
 * Inherits from the [Component] abstract class.
 *
 * @param name The name of the RAComponent.
 * @param percentage The weightage or percentage of the RAComponent.
 */
class RAComponent(name: String, percentage: Double) : Component(name, percentage)