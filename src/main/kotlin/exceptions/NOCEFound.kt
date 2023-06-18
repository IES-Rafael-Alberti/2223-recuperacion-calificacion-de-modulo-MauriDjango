package exceptions

object NOCEFound : Throwable() {
    override val message: String = "No CE's were found"
}
