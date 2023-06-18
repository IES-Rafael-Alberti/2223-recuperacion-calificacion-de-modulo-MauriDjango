package exceptions

object NoRAFound : Throwable() {
    override val message: String = "No RA was found"
}
