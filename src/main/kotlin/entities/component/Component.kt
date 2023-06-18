package entities.component

/**
 * Abstract class representing a component of a larger entity,
 * such as a grade component or a module component.
 *
 * @param name The name of the component.
 * @param percentage The weightage or percentage of the component.
 */
abstract class Component(val name: String, val percentage: Double)