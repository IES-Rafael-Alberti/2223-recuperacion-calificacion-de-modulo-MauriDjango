package exceptions

object CENotFound : Throwable() {
    override val message = "There was an error reading Criterios de Evaluacion 'CE' from ModCSVFile"
}
