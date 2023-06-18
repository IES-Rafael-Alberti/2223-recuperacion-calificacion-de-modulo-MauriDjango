package exceptions

object GetCEError : Throwable() {
    override val message: String = "There was an error reading CE from the file"
}
