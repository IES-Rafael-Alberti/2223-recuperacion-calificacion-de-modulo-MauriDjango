package entities.component


/**
 * Represents a specific CE (Course Evaluation) component, which is a type of component associated with a CE grade.
 * Inherits from the [Component] abstract class.
 *
 * @param name The name of the CE component.
 * @param percentage The weightage or percentage of the CE component.
 */
class CEComponent(name: String, percentage: Double) : Component(name, percentage)
