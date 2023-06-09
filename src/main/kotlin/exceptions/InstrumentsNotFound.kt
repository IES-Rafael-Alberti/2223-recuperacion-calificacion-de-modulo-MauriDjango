package exceptions

object InstrumentsNotFound : Throwable() {
    override val message = "No instrument was found in the file"
}
