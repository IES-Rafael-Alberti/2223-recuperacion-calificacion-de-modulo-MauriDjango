package assembler.csvAssembler

import assembler.Assembler
import dataSource.CSVDSource
import entities.grade.*
import exceptions.StudentEmpty


/**
 * This class inherits from Assembler so that we know what it's purpose is (To assemble)
 *
 * @param csvHandler: An object that extracts the data in a .csv file
 */
class CSVStudentAssembler(private val connection: CSVDSource): Assembler<Student>() {

    /**
     * Creates a list of Student objects for
     * each student name found in the CSVHandler that is not already in existingStudents by name
     * and the students objects that are already stored in students
     *
     * @param students: A list of students that have already been created
     * @return students: The original list passed with new students added
     */
    override fun assemble(component: Student) {
        TODO("NOT YET IMPLEMENTED")
    }

    fun updateStudents(students: MutableList<Student>) {

        connection.getStudents().forEach { studentName ->
            students.find { it.name == studentName } ?: run {
                Student(studentName).let{ student ->
                    students.add(student)
                }
            }
        }
        if (students.isEmpty()) throw StudentEmpty
    }
}


