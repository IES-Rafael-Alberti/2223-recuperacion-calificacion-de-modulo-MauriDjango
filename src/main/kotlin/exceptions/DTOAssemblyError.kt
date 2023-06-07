package exceptions

object DTOAssemblyError : Throwable() {
    override val message = "There was an error assembling the DTO"
}
