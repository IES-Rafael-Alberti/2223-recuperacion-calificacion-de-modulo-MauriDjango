import assembler.csvAssembler.CSVAssemblerImpl
import csv.CSVHandler
import csv.getCSVFiles
import display.Display
import entities.grade.Student
import utilities.MainArgs
import org.slf4j.LoggerFactory
import dao.DAOUtilities
import dataSource.CSVDSource
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
    val path = MainArgs.getPath(args)
    val csvFiles: MutableList<File> = mutableListOf()
    val csvHandlers: MutableList<CSVHandler> = mutableListOf()
    val csvDSources: MutableList<CSVDSource> = mutableListOf()
    var students: MutableList<Student> = mutableListOf()

    //Adds files in directory to csvFiles
    path?.let { pathString ->
        csvFiles.addAll(getCSVFiles(pathString))
        logger.debug("CSVFiles created")
    }

    //Converts CSVFiles to CSVHandlers
    csvFiles.forEach { csvFile ->
        CSVHandler(csvFile).let {csvHandler ->
            CSVDSource(csvHandler).let { csvDSource ->
                csvDSources.add(csvDSource)
            }
        }
    }
    logger.debug("CSVReaders created")

    //This next part of database accessing is done regardless if the db option has been selected or not
    //to avoid unnecessarily creating multiple instances of the same object

    //Creates tables
    DAOUtilities.generateTables()

    //If students in database assigns to students else students is emptyList
    students = DAOUtilities.retrieveDBObjects()

    //Extracts Data from CSVFiles and updates students with new grades
    csvDSources.forEach { csvDSource ->
        CSVAssemblerImpl(csvDSource, MainArgs.getModulo(args), students).assembleAll()
    }
    logger.debug("CSV data extracted into students")

    //Determines database behaviour based on args
    when (MainArgs.getDB(args)) {
        "" -> DAOUtilities.insertDBObjects(students)
        "d" -> DAOUtilities.deleteAll()
        "q" -> students = DAOUtilities.retrieveDBObjects()
    }

    //Displays student information in console
    students.forEach {student ->
        student.modulos.find { it.moduloName == MainArgs.getModulo(args) }?.let { modGrade ->
            Display.printTable(Display.toTable(student, modGrade))
            modGrade.subComponents.forEach { raGrade ->
                Display.printTable(Display.toTable(raGrade))
            }
        }
    }
}



