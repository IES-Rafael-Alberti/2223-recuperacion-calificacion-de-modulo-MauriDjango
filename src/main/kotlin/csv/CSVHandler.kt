package csv

import bingo.inputoutput.exceptions.FileEmpty
import entities.component.Component
import entities.grade.Grade
import entities.grade.Student
import exceptions.*
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder


//TODO temporary... Later on filePath will be passed as Main Argument
//TODO extract line iteration in methods to single function
/**
 * A class in that extracts data from a csvFile
 * The csvFile should contain information of a Resultado de Aprendizaje
 *
 * @property logger = logger
 * @property lines = A list of each line of the csvFile
 * @property linesList = A list of lines that have been split by their commas
 * @property raPercentIndex = Index for the column that contains RA percentages
 * @property cePercentIndex = Index for the column that contains CE percentages
 * @property gradeSectionIndex = Index for the first student column
 *
 * @param csvFile: The file which will be read
 */
class CSVHandler(private val csvFile: File) {
    private val logger = LoggerFactory.getLogger("CSVDAO")
    private var lines: List<String> = csvFile.readLines()
    private var linesList: MutableList<MutableList<String>>
    private var raPercentage: Double? = null
    private var raPercentIndex: Int? = null
    private var cePercentIndex: Int? = null
    private var gradeSectionIndex: Int? = null

    init {
        if (lines.isEmpty()) throw FileEmpty
        linesList = linesToList()
        raPercentIndex = findIndex("%RA")
        cePercentIndex = findIndex("%CE")?.plus(1)
        gradeSectionIndex = findIndex("%RA")?.plus(1)
        raPercentage = getRAPercentage()


    }

    /**
     * Finds the index of a passed string
     * or returns null if no match is found
     *
     * @param string: String to be matched
     * @return index: Index if found or null if not found
     */
     private fun findIndex(string: String): Int? {
        var index: Int? = null

        linesList.forEach() {list ->
            list.indexOf(string).let {
                if (it > -1) {
                    index = it
                }
            }
        }
         if (index == null) {
             throw IndexNotFound
         }
        return index
    }

    /**
     * Finds the index for a specific Criterio de Evaluacion "CE" row
     *
     * @param string: CE name to be matched
     * @return index: Index if found or null if not found
     */
    private fun findCEIndex(string: String): Int? {
        val ceRegex = Regex("UD\\d*\\.($string)")
        var index: Int? = null

        linesList.forEach { line ->
            line.forEach { element ->
                ceRegex.find(element)?.let {
                    index = linesList.indexOf(line)
                }
            }
        }
        if (index == null) throw CENotFound
        return index
    }

    /**
     * Finds the index for the Resultado de Aprendizaje "RA" row
     *
     * @param string: RA name to be matched
     * @return index: Index if found or null if not found
     */
    private fun findRAIndex(): Int? {
        val raRegex = Regex("RA(\\d)")
        var index: Int? = null

        linesList.forEach { line ->
            line.forEach { element ->
                raRegex.matchEntire(element)?.let {
                    index = linesList.indexOf(line)
                }
            }
        }
        if (index == null) throw CENotFound
        return index
    }

    /**
     * Converts lines property to a list of lines split respecting the .csv raw format
     *
     * @return allLines: Lines split
     */
    private fun linesToList(): MutableList<MutableList<String>> {
        val allLines: MutableList<MutableList<String>> = mutableListOf()
        val lineListed: MutableList<String> = mutableListOf()
        val string = StringBuilder()
        var quotesOpen = false

        lines.forEach() { line ->
            line.forEach { char ->
                if (char == ',' && !quotesOpen) {
                    lineListed.add(string.toString())
                    string.clear()
                } else if (char == '"') {
                    quotesOpen = !quotesOpen
                } else
                    string.append(char)
            }
            lineListed.add(string.toString()) //Collects last string not followed by comma
            string.clear()

            allLines.add(lineListed.toMutableList())
            lineListed.clear()
        }
        if (allLines.isEmpty()) throw LinesToListError
        return allLines
    }

    /**
     * Gets a list of names for all the students in CSVHandler
     *
     * @return studentList: List of student's names
     */
    fun getStudents(): MutableList<String> {
        val studentList = mutableListOf<String>()
        val firstStudentIndex: Int?

        firstStudentIndex = lines[0].split(',').indexOfFirst { it != "" }

        lines[1].split(',').drop(firstStudentIndex).forEach() {
            if (it != ",") studentList.add(it)
        }

        if (studentList.isEmpty()) throw StudentsNotFound
        return studentList
    }

    /**
     * Gets the RA number and percentage from CSVHandler
     *
     * @return ra: Pair<name: String, percentage: String>
     */
    fun getRAComponent(): Pair<String, Double>? {
        val raRegex = Regex("RA(\\d)")
        var ra: Pair<String, Double>? = null

        linesList.forEach { line ->
            line.forEach { element ->
                raRegex.matchEntire(element)?.let { matchResult ->
                    if (raPercentIndex != null) {
                        ra?.let { ra ->
                        if (ra.first != matchResult.groupValues[1]) throw MultipleRAComponentsFound
                        }
                        raPercentage?.let { ra = Pair(matchResult.groupValues[1], it) }
                    }
                }
            }
        }
        if (ra == null) throw NoRAFound
        return ra
    }

    /**
     * Retrieves the percentage value for a specific RA (Resultado Aprendizaje) from the lines list.
     *
     * The function iterates through each line in the lines list and searches for a matching element that
     * represents a RA using the "UD" pattern followed by digits. If a match is found, the function extracts
     * the percentage value from the specified column index and returns it as a Double.
     *
     * @return The percentage value for the RA, or null if it couldn't be found.
     * @throws NoRAPercentFound if no percentage value for the RA was found in the lines list.
     */
    private fun getRAPercentage(): Double? {
        val udRegexRow = Regex("UD\\d+")
        var percentage: Double? = null

        linesList.forEach { line ->
            line.forEach { element ->
                udRegexRow.matchEntire(element)?.let {
                    raPercentIndex?.let { percentage = CSVUtil.stringToDouble(line[it]) }
                }
            }
        }
        percentage?: {throw NoRAPercentFound }
        return percentage
    }

    /**
     * Gets a list of all the CE names in the CSVHandler
     *
     * @return ceList: List of Pair<name: String, percentage: String>
     */
    fun getCEComponents(): MutableList<Pair<String, String>> {
        val ceRegex = Regex("UD\\d*\\.([a-z])")
        val ceList: MutableList<Pair<String, String>> = mutableListOf()

        try {
            linesList.forEach() { line ->
                line.forEach() { element ->
                    ceRegex.find(element)?.let {
                        cePercentIndex?.let { percent ->
                            ceList.add(
                                Pair(
                                    it.groupValues[1],
                                    line[percent]
                                )
                            )
                        }
                    }
                }
            }
            if (ceList.isEmpty()) throw NOCEFound
        } catch (e: Exception) {
            throw GetCEError
        }
        return ceList
    }

    /**
     * Gets a list of all the Instrument names in the CSVHandler
     *
     * @return instruments: List of Pair<name: String, percentage: String>
     */
    fun getInstrumentComponents(): MutableList<Pair<String, String>> {
        val instrumentRegex = Regex("([a-z],[a-z])")
        val instruments = mutableListOf<Pair<String, String>>()

                    linesList.forEach { line ->
                        line.forEach { element ->
                            instrumentRegex.find(element)?.let { instMatch ->
                                raPercentIndex?.let { percentIndex ->
                                    instruments.add(
                                        Pair(
                                            "${element}${linesList.indexOf(line)}",
                                            line[percentIndex]
                                        )
                                    )
                                }
                            }
                        }
                    }



        if (instruments.isEmpty()) {
            throw InstrumentComponentsEmpty
        }
        return instruments
    }

    /**
     * Gets the grade for a students instrument grade
     *
     * @param studentName: Students name
     * @param component: InstrumentComponent of instrument that is being searched for
     * @return grade: Grade that was found or null if not found
     */
    fun getInstrumentGrade(studentName: String, component: Component): String? {
        var grade: String? = null
        val instrumentRegex = Regex("([a-z],[a-z])")

        linesList.forEach { line ->
            line.forEach { element ->
                instrumentRegex.find(element)?.let {
                    findIndex(studentName)?.let { studentIndex ->
                        if (component.name == "${element}${linesList.indexOf(line)}") {
                            grade = line[studentIndex]
                        }
                    }
                }
            }
        }
        if (grade == null) {
            throw InstrumentGradeEmpty
        }
        return grade
    }

    /**
     * Updates linesList property with CE grades of a list of students
     *
     * @param students: List of Students objects
     */
    fun updateCEGrades(students: MutableList<Student>) {
        val ra: String? = getRAComponent()?.first
        val newLinesList: MutableList<MutableList<String>> = linesList.toMutableList()
        var matchedRAGrades: Grade? = null

        students.forEach { student ->
            student.modulos.forEach { modulo ->
                modulo.subComponents.forEach { raGrade ->
                    if (raGrade.component.name == ra) {
                        matchedRAGrades = raGrade
                    }
                }
            }

            findIndex(student.name)?.let { studentIndex ->
                matchedRAGrades?.subComponents?.forEach { ce ->
                    findCEIndex(ce.component.name)?.let { ceIndex ->
                        newLinesList[ceIndex][studentIndex] = ce.getGrade().toString()
                    }
                }
            }
        }
        linesList.clear()
        linesList.addAll(newLinesList)
    }

    /**
     * Updates linesList property with RA grades of a list of students
     *
     * @param students: List of Students objects
     */
    fun updateRAGrades(students: MutableList<Student>) {
        val ra: String? = getRAComponent()?.first
        val newLinesList: MutableList<MutableList<String>> = linesList.toMutableList()
        var mathcedRAGrade: Grade? = null

        students.forEach { student ->
            student.modulos.forEach { modulo ->
                modulo.subComponents.forEach { raGrade ->
                    if (raGrade.component.name == ra) {
                        mathcedRAGrade = raGrade
                    }
                }
            }

            findIndex(student.name)?.let { studentIndex ->
                mathcedRAGrade?.let {
                    findRAIndex()?.let { raIndex ->
                        newLinesList[raIndex][studentIndex] = mathcedRAGrade?.getGrade().toString()
                    }
                }
            }
        }
        linesList.clear()
        linesList.addAll(newLinesList)
    }

    /**
     * Overwrites csvFile contents using linesList property
     */
    private fun overwriteFile() {
        val fileWriter = csvFile.bufferedWriter()

        fileWriter.use { writer ->
            linesList.forEach { line ->
                line.forEach { element ->
                    if (',' in element) {
                        line[line.indexOf(element)] = "\"$element\""
                    }
                }
                val modifiedLine = line.joinToString(",")
                writer.write(modifiedLine)
                writer.newLine()
            }
        }
    }

    /**
     * Updates the CSV file with the latest grades of the students.
     *
     * @param students The list of students whose grades need to be updated.
     */
    fun updateCSVFile(
        students: MutableList<Student>,
    ) {

        updateCEGrades(students)
        updateRAGrades(students)
        overwriteFile()
        logger.debug("CSVFile updated")
    }

    fun getStudentInitials(studentName: String): String {
        var studentInitials = ""
        linesList.forEach { line ->
            line.find { it == studentName }?.let {
                line.indexOf(studentName) }?.let {studentIndex ->
                    studentInitials = linesList[0][studentIndex]
                }
            }
        return studentInitials
    }
}