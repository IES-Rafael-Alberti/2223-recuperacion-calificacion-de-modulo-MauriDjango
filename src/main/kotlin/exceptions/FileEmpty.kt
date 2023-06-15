package bingo.inputoutput.exceptions

object FileEmpty : Throwable() {
    val msg = "CSV file was accedsed but was empty."
}
