package exceptions

object InstrumentComponentsEmpty : Throwable() {
    override val message: String = "No instrument components were found"
}
