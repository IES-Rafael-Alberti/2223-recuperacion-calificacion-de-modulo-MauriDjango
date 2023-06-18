package exceptions

object StringToDoubleDefault : Throwable() {
    override val message: String = "There was an error changing the string to double"
}
