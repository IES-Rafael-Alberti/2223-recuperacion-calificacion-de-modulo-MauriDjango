package exceptions

object NoPathFound : Throwable() {
    override val message: String = "No path was found"
}
