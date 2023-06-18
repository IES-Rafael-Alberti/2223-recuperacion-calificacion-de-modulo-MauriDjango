package assembler


/**
 * This class helps with the instantiation of multiple classes that are related to each other
 */
abstract class Assembler<T>() {
    abstract fun assemble(component: T)
}
