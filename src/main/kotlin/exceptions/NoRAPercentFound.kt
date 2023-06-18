package exceptions

object NoRAPercentFound : Throwable() {
    override val message: String = "No RA percentage was found"
}
