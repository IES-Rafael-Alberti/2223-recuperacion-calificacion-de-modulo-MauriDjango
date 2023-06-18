package exceptions

object NOCSVFilesFound : Throwable() {
    override val message: String = "No csv files were found"
}
