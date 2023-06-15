import Assembler.StudentAssembler
import csv.CSVReader
import csv.getCSVFiles
import display.Display
import entities.grade.Student
import exceptions.NoCSVFile
import exceptions.NoPathFound
import main.MainArgs
import org.slf4j.LoggerFactory
import java.io.File


fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger("Main")
    val mainArgs = MainArgs(args)
    val path = mainArgs.getPath()
    val csvFiles: MutableList<File> = mutableListOf()
    val csvReaders: MutableList<CSVReader> = mutableListOf()
    var students: MutableList<Student> = mutableListOf()

    if (path == null) throw NoPathFound
    else
    {
        csvFiles.addAll(getCSVFiles(path))
        logger.debug("CSVFiles created")
    }

    if (csvFiles.isEmpty()) throw NoCSVFile
    else {
        csvFiles.forEach {
            csvReaders.add(CSVReader(it))
        }
        logger.debug("CSVReaders created")
    }

    when (mainArgs.getDB()) {
        0 -> {
            csvReaders.forEach { csvReader ->
                val studentAssembler = StudentAssembler(csvReader)

                students = studentAssembler.getStudents(students)
                students.forEach { student ->
                    studentAssembler.addStudentModulos(student, mainArgs.getModulo())
                }
                csvReader.updateCEGrades(students)
                csvReader.updateRAGrades(students)
                csvReader.writeNewGrades()
            }
            logger.debug("Students initiated through CSV")
        }

        1 -> {
            csvReaders.forEach { csvReader ->
                val studentAssembler = StudentAssembler(csvReader)

                students = studentAssembler.getStudents(students)
                students.forEach { student ->
                    studentAssembler.addStudentModulos(student, mainArgs.getModulo())
                }
                csvReader.updateCEGrades(students)
                csvReader.updateRAGrades(students)
                csvReader.writeNewGrades()
            }
            logger.debug("Students initiated through CSV")
            mainArgs.getDBOption(mainArgs.getDB(), students)
        }
        2 -> mainArgs.getDB()
        3 -> {
            mainArgs.getDBOption(mainArgs.getDB())?.let { dbStudents ->
                students = dbStudents
            }
            logger.debug("Students initiated through db")
        }
    }
    students.forEach {student ->
        student.modulos.find { it.moduloName == mainArgs.getModulo() }?.let { modGrade ->
            Display.printTable(Display.toTable(student, modGrade))
            modGrade.subComponents.forEach { raGrade ->
                Display.printTable(Display.toTable(raGrade))
            }
        }
    }
}

