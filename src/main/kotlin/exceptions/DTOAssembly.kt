package exceptions

object DTOAssembly : Throwable() {
    override val message = "There was an error assembling the DTO"
}
