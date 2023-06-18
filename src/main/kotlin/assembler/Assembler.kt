package assembler


/**
 * The base abstract class for an assembler.
 *
 * @param T The type of component to assemble.
 */
abstract class Assembler<T>() {

    /**
     * Assembles the given component.
     *
     * @param component The component to assemble.
     */
    abstract fun assemble(component: T)
}
