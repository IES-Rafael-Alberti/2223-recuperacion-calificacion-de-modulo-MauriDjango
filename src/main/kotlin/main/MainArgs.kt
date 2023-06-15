package main

import dao.*
import entities.grade.Modulo
import entities.grade.Student
import exceptions.GetDBOptionError
import exceptions.StudentEmpty
import org.slf4j.LoggerFactory

const val moduloArgument = "-mo"
const val pathArgument = "-pi"
const val dbArgument = "-bd"

class MainArgs(private val args: Array<String>) {
    val logger = LoggerFactory.getLogger("MainArgs")

    fun getModulo(): String {
        var modulo: String = "Programacion"
        if (moduloArgument in args) {
            modulo = args[args.indexOf(moduloArgument) + 1]
        }
        return modulo
    }

    fun getPath(): String? {
        var path: String? = null
        if (pathArgument in args) {
            path = args[args.indexOf(pathArgument) + 1]
        }
        return path
    }

    fun getDB(): Int {
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
    }

    fun getDBOption(option: Int, students: MutableList<Student>? = null): MutableList<Student>? {

        return when (option) {
            1 -> {
                students?.let {generateDataClasses(it)}
                return null
            }
            2 -> {
                deleteAll()
                return null
            }
            3 -> retreiveDataClasses()
            else -> {throw GetDBOptionError
            }
        }
    }

    private fun generateTables() {
        studentDAO.createTable()
        moduloDAO.createTable()
        raDAO.createTable()
        ceDAO.createTable()
        instrumentDAO.createTable()
    }

    private fun generateDataClasses(students: MutableList<Student>) {

        students.forEach { student ->
            studentDAO.create(student)
            student.modulos.forEach { modulo ->
                moduloDAO.create(modulo)
                modulo.subComponents.forEach { ra ->
                    raDAO.create(ra)
                    ra.subComponents.forEach { ce ->
                        ceDAO.create(ce)
                        ce.subComponents.forEach { instrument ->
                            instrumentDAO.create(instrument)
                        }
                    }
                }
            }
        }
    }

    private fun retreiveDataClasses(): MutableList<Student> {
        val studentLists: MutableList<Student> = mutableListOf()

        studentDAO.getAll().forEach { student ->
            studentLists.add(student)
            moduloDAO.getAll().forEach { modulo ->
                if (student.id == modulo.superComponentID) {
                    student.apply { this.modulos.add(modulo as Modulo) }
                }
                raDAO.getAll().forEach { raGrade ->
                    if (raGrade.superComponentID == modulo.id) {
                        modulo.apply { this.subComponents.add(raGrade) }
                    }
                    ceDAO.getAll().forEach { ceGrade ->
                        if (raGrade.id == ceGrade.superComponentID) {
                            ceGrade.subComponents.add(raGrade)
                        }
                        instrumentDAO.getAll().forEach { instrumentGrade ->
                            if (ceGrade.id == instrumentGrade.superComponentID) {
                                ceGrade.subComponents.add(instrumentGrade)
                            }
                        }
                    }
                }
            }
        }
        return studentLists
    }

    private fun deleteAll() {
        studentDAO.deleteAll()
        moduloDAO.deleteAll()
        raDAO.deleteAll()
        ceDAO.deleteAll()
        instrumentDAO.deleteAll()
    }
}
