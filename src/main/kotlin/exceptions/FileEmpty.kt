package bingo.inputoutput.exceptions

object FileEmpty : Throwable() {
    override val message = "CSV file was accedsed but was empty."
}
