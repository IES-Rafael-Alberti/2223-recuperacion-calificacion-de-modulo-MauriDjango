package exceptions

object StudentsNotFound : Throwable() {
    override val message = "There was an issue reading students from ModCSVFile"
}
