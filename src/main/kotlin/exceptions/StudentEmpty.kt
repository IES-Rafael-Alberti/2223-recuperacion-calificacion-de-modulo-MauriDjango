package exceptions

object StudentEmpty : Throwable() {
    override val message: String = "No students have been added"
}
