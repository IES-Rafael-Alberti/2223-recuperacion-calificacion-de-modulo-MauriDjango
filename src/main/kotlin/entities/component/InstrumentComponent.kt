package entities.component


/**
 * Represents a specific instrument component, which is a type of component associated with an instrument grade.
 * Inherits from the [Component] abstract class.
 *
 * @param name The name of the instrument component.
 * @param percentage The weightage or percentage of the instrument component.
 */
class InstrumentComponent(name: String, percentage: Double) : Component(name, percentage)

