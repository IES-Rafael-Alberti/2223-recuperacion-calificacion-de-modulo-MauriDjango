package mainUtil

import exceptions.NoPathFound
import org.slf4j.LoggerFactory

const val moduloArgument = "-mo"
const val pathArgument = "-pi"
const val dbArgument = "-bd"


/**
 * A class that handles the arguments passed to main
 *
 * @param args: Arguments passed to main
 * @property logger: Logger
 */
object MainArgs {
    private val logger = LoggerFactory.getLogger("MainArgs")

    /**
     * Gets the name of the modulo passed in args
     * if no name is passed returns a default "PRO"
     *
     * @return modulo: Name of the modulo
     */
    fun getModulo(args: Array<String>): String {
        var modulo: String = "PRO"
        if (moduloArgument in args) {
            modulo = args[args.indexOf(moduloArgument) + 1]
        }
        return modulo
    }

    /**
     * Gets the path passed in args
     *
     * @return path: Path if found in args or null
     */
    fun getPath(args: Array<String>): String? {
        var path: String? = null
        if (pathArgument in args) {
            path = args[args.indexOf(pathArgument) + 1]
        }
        if (path == null) throw NoPathFound
        return path
    }

    /**
     * Processes database option pass through args
     *
     * @return option =
     */
    fun getDB(args: Array<String>): String? {
        var option: String? = null
        try {
            if (dbArgument in args) {
                option = ""
                if (args[args.indexOf(dbArgument) + 1] == "d") {
                    option = "d"
                }
                if (args[args.indexOf(dbArgument) + 1] == "q") {
                    option = "q"
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            logger.warn(e.message)
        }
        return option
    }
}

