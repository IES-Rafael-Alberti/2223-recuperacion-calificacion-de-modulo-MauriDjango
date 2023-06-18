package exceptions

object LinesToListError : Throwable() {
    override val message: String = "There was an error converting the csv line to a list of lists"
}
