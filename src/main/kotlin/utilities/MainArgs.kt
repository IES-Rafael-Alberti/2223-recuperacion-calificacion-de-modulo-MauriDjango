package utilities

import entities.grade.Student
import exceptions.GetDBOptionError
import exceptions.NoPathFound
import org.slf4j.LoggerFactory
import dao.DAOUtilities

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
    /*    fun getDB(): Int {
        var option = 0
        try {
            if (dbArgument in args) {
                generateTables()
                option = 1
                if (args[args.indexOf(dbArgument) + 1] == "d") {
                    option = 2
                    getDBOption(option = option)
                }
                if (args[args.indexOf(dbArgument) + 1] == "q") {
                    option = 3
                    getDBOption(option = option)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            logger.warn(e.message)
        }
        return option
    }*/


    fun getDBOption(option: Int, students: MutableList<Student>? = null): MutableList<Student>? {

        return when (option) {
            1 -> {
                students?.let { DAOUtilities.createDBObjects(it)}
                return null
            }
            2 -> {
                DAOUtilities.deleteAll()
                return null
            }
            3 -> DAOUtilities.retrieveDBObjects()
            else -> {throw GetDBOptionError
            }
        }
    }




}

