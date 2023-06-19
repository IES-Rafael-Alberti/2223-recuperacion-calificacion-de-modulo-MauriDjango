package dataSource

import entities.component.Component
import entities.grade.Student


/**
 * Data source implementation for CSV data.
 *
 * @param connection The CSVHandler connection used for data retrieval.
 */
class CSVDSource(connection: csv.CSVHandler): DSource<csv.CSVHandler>(connection) {

    /**
     * Retrieves the list of student names from the CSVHandler.
     *
     * @return The list of student names.
     */
    fun getStudents() = connection.getStudents()

    /**
     * Retrieves the RA component data from the CSVHandler.
     *
     * @return The RA component data as a pair of component name and description.
     */
    fun getRAComponent() = connection.getRAComponent()

    /**
     * Retrieves the CE component data from the CSVHandler.
     *
     * @return The list of CE component data as pairs of component name and description.
     */
    fun getCEComponents() = connection.getCEComponents()

    /**
     * Retrieves the instrument component data from the CSVHandler.
     *
     * @return The list of instrument component data as pairs of component name and description.
     */
    fun getInstrumentComponents() = connection.getInstrumentComponents()

    /**
     * Retrieves the instrument grade for the specified student and component from the CSVHandler.
     *
     * @param studentName The name of the student.
     * @param component The instrument component.
     * @return The instrument grade for the student and component.
     */
    fun getInstrumentGrade(studentName: String, component: Component) = connection.getInstrumentGrade(studentName, component)

    /**
     * Retrieves the initials of a student based on their full name.
     *
     * @param studentName The full name of the student.
     * @return The initials of the student.
     */
    fun getStudentInitials(studentName: String): String = connection.getStudentInitials(studentName)

    /**
     * Updates the CSV file with the data of the provided students for a specific modulo.
     *
     * @param students The list of students to update in the CSV file.
     * @param moduloName The name of the modulo for which the students' data is being updated.
     */
    fun updateCSV(students: MutableList<Student>, moduloName: String) = connection.updateCSVFile(students, moduloName)
}
