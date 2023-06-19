package assembler.csvAssembler

import assembler.Assembler
import dataSource.CSVDSource
import entities.grade.*
import exceptions.StudentEmpty


/**
 * CSVStudentAssembler is responsible for assembling CSV data for Student objects.
 *
 * @property connection The CSVDSource connection for retrieving CSV data.
 */
class CSVStudentAssembler(private val connection: CSVDSource): Assembler<Student>() {

    /**
     * Creates a list of Student objects for each student name found in the CSVHandler that is not already in existingStudents by name
     * and the students objects that are already stored in students.
     *
     * @param component The Student object to assemble.
     */
    override fun assemble(component: Student) {
        TODO("NOT YET IMPLEMENTED")
    }

    /**
     * Updates the list of students with new students from the CSVHandler that are not already present.
     *
     * @param students The list of students that have already been created.
     * @throws StudentEmptyException if no students are found in the CSVHandler.
     */
    fun updateStudents(students: MutableList<Student>) {

        connection.getStudents().forEach { studentName ->
            students.find { it.name == studentName } ?: run {
                connection.getStudentInitials(studentName).let { initials ->
                    Student(studentName, initials).let { student ->
                        students.add(student)
                    }
                }
            }
        }
        if (students.isEmpty()) throw StudentEmpty
    }
}
