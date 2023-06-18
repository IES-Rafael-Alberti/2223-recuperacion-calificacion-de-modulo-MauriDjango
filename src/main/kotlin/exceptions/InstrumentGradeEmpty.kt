package exceptions

object InstrumentGradeEmpty : Throwable() {
    override val message: String = "No instrument grades were found"
}
