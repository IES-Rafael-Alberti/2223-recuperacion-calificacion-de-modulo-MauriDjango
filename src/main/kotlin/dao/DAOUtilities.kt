package dao

import entities.grade.Modulo
import entities.grade.Student
import org.slf4j.LoggerFactory


/**
 * A utility class for managing DAO operations and interacting with the database.
 */
object DAOUtilities {
    val logger = LoggerFactory.getLogger("DAOUtilities")


    /**
     * Generates database tables for various entities.
     */
    fun generateTables() {
        studentDAO.createTable()
        moduloDAO.createTable()
        raDAO.createTable()
        ceDAO.createTable()
        instrumentDAO.createTable()
        logger.debug("Tables generated")
    }

    /**
     * Creates database objects for the provided list of students and their associated entities.
     *
     * @param students The list of students to create in the database.
     */
    fun createDBObjects(students: MutableList<Student>) {

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
        logger.debug("Objects inserted into Database")
    }

    /**
     * Retrieves database objects and their associated entities and constructs them into student objects.
     *
     * @return The list of retrieved students with their associated entities populated.
     */
    fun retrieveDBObjects(): MutableList<Student> {
        val studentLists: MutableList<Student> = mutableListOf()
        val students = studentDAO.getAll()
        val modulos = moduloDAO.getAll()
        val ras = raDAO.getAll()
        val ces = ceDAO.getAll()
        val instruments = instrumentDAO.getAll()

        students.forEach { student ->
            studentLists.add(student)
            modulos.forEach { modulo ->
                if (student.id == modulo.superComponentID) {
                    student.modulos.add(modulo as Modulo)
                    ras.forEach { raGrade ->
                        if (raGrade.superComponentID == modulo.id) {
                            modulo.subComponents.add(raGrade)
                            ces.forEach { ceGrade ->
                                if (raGrade.id == ceGrade.superComponentID) {
                                    raGrade.subComponents.add(ceGrade)
                                    instruments.forEach { instrumentGrade ->
                                        if (ceGrade.id == instrumentGrade.superComponentID) {
                                            ceGrade.subComponents.add(instrumentGrade)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        logger.debug("DB objects retrieved")
        return studentLists
    }

    /**
     * Deletes all database objects and associated entities.
     */
    fun deleteAll() {
        studentDAO.deleteAll()
        moduloDAO.deleteAll()
        raDAO.deleteAll()
        ceDAO.deleteAll()
        instrumentDAO.deleteAll()
        logger.debug("All DB objects deleted")
    }
}