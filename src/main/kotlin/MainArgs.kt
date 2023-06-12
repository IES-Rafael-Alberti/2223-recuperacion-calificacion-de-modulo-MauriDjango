

val mainArgs = MainArgs(arrayOf("Programming"))

class MainArgs(private val args: Array<String>) {
    fun getModulo(): String = args[0]
}