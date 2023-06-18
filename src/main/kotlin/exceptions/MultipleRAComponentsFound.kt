package exceptions

object MultipleRAComponentsFound : Throwable() {
    override val message: String = "Multiple non matching RA's were found in the csv"
}
