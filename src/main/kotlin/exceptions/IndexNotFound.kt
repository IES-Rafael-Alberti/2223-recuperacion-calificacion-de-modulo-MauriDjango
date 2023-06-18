package exceptions

object IndexNotFound : Throwable() {
    override val message: String = "There was an error finding the index"
}
