import Assembler.StudentAssembler
import csv.CSVHandler
import csv.getCSVFiles
import display.Display
import entities.grade.Student
import utilities.MainArgs
import org.slf4j.LoggerFactory
import dao.DAOUtilities
import java.io.File


/**
 * This is an application that takes in data of Resultados de Aprendizaje in the form of .csv files
 * It calculates the grades of the students in the file
 * Has the option to implement a database to store file data
 *
 * @param args: -mo =
 *      modulo, -pi = .csv directory path,
 *      -bd [d, q] = database options [d: delete all, q: show student database data]
 */
fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("Main")
    val mainArgs = MainArgs(args)
    val path = mainArgs.getPath()
    val csvFiles: MutableList<File> = mutableListOf()
    val csvHandlers: MutableList<CSVHandler> = mutableListOf()
    var students: MutableList<Student> = mutableListOf()

    //Adds files in directory to csvFiles
    path?.let { pathString ->
        csvFiles.addAll(getCSVFiles(pathString))
        logger.debug("CSVFiles created")
    }

    //Converts CSVFiles to CSVHandlers
    csvFiles.forEach {
        csvHandlers.add(CSVHandler(it))
    }
    logger.debug("CSVReaders created")

    //Creates tables
    DAOUtilities.generateTables()
    //If students in database assigns to students else student is emptyList
    students = DAOUtilities.retrieveDBObjects()

    //Extracts Data from CSVFiles and updates students with new grades
    csvHandlers.forEach { csvHandler ->
        val studentAssembler = StudentAssembler(csvHandler)
        students = studentAssembler.getStudents(students, mainArgs.getModulo())
        logger.debug("Students updated by CSV")
        csvHandler.updateCSVFile(students, studentAssembler, mainArgs)
    }
    logger.debug("CSV data extracted to students")


    //Determines database behaviour based on args
    when (mainArgs.getDB()) {
        "" -> {
            students.let { DAOUtilities.createDBObjects(it)}
        }
        "d" -> DAOUtilities.deleteAll()
        "q" -> students = DAOUtilities.retrieveDBObjects()
    }

    //Displays student information in console
    students.forEach {student ->
        student.modulos.find { it.moduloName == mainArgs.getModulo() }?.let { modGrade ->
            Display.printTable(Display.toTable(student, modGrade))
            modGrade.subComponents.forEach { raGrade ->
                Display.printTable(Display.toTable(raGrade))
            }
        }
    }
}



