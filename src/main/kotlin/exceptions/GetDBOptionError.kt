package exceptions

object GetDBOptionError : Throwable() {
    override val message: String = "There was an error reading the db option from main argument"
}
