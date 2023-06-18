package entities.component

/**
 * Abstract class representing a component of a larger entity,
 * such as a grade component or a module component.
 *
 * @param componentName The name of the component.
 * @param percentage The weightage or percentage of the component.
 */
abstract class Component(val componentName: String, val percentage: Double)