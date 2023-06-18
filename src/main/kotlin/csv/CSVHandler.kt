package csv

import assembler.csvAssembler.CSVStudentAssembler
import bingo.inputoutput.exceptions.FileEmpty
import entities.component.Component
import entities.grade.Grade
import entities.grade.Student
import exceptions.*
import utilities.MainArgs
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

    init {
        if (lines.isEmpty()) throw FileEmpty
    }

    private val linesList: MutableList<MutableList<String>> = linesToList()
    private val raPercentIndex = findIndex("%RA")
    private val cePercentIndex = findIndex("%CE")?.plus(1)
    private val gradeSectionIndex = findIndex("%RA")?.plus(1)

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
    private fun findRAIndex(string: String): Int? {
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
    fun getRAComponent(): Pair<String, String>? {
        val raRegex = Regex("RA(\\d)")
        var ra: Pair<String, String>? = null

        linesList.forEach { line ->
            line.forEach { element ->
                raRegex.matchEntire(element)?.let { matchResult ->
                    if (raPercentIndex != null) {
                        if (ra == null) ra = Pair(matchResult.groupValues[1], line[raPercentIndex])
                        else throw MultipleRAComponentsFound
                    }
                }
            }
        }
        if (ra == null) throw NoRAFound
        return ra
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
                        if (cePercentIndex != null) {
                            ceList.add(
                                Pair(
                                    it.groupValues[1],
                                    line[cePercentIndex]
                                )
                            )
                        } else throw CEPercentIndex
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
        val instrumentNameRegex = Regex("([a-z],[a-z])\\D+([\\d\\+]+)")
        val instruments = mutableListOf<Pair<String, String>>()

        lines.forEach { line ->
            instrumentNameRegex.find(line)?.let { instNameMatch ->
                lines.indexOf(line).let { index ->
                    linesList[index].forEach { element ->
                        instrumentRegex.find(element)?.let { instMatch ->
                            if (raPercentIndex != null) {
                                instruments.add(
                                    Pair(
                                        "($element)${instNameMatch.groupValues[2]}",
                                        linesList[index][raPercentIndex]
                                    )
                                )
                            } else throw RAPercentIndex
                        }
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
        val studentIndex = findIndex(studentName)
        val instrumentRegex = Regex("([a-z],[a-z])")
        val instrumentNameRegex = Regex("([a-z],[a-z])\\D+([\\d\\+]+)")

        lines.forEach { line ->
            instrumentNameRegex.find(line)?.let { instNameMatch ->
                lines.indexOf(line).let { index ->
                    linesList[index].forEach { element ->
                        instrumentRegex.find(element)?.let { instMatch ->
                            if (studentIndex != null && raPercentIndex !== null) {
                                if (component.name == "($element)${instNameMatch.groupValues[2]}") {
                                    grade = linesList[index][studentIndex]
                                }
                            }
                        }
                    }
                }
            }
        }
/*        linesList.forEach { line ->
            line.forEach { element ->
                instrumentRegex.find(element)?.let {
                    if (component.componentName == element) {
                        if (studentIndex != null && raPercentIndex !== null) {
                            grade = line[studentIndex]
                        }
                    }
                }
            }
        }*/
        if (grade == null) throw InstrumentGradeEmpty
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
                mathcedRAGrade?.subComponents?.forEach { ce ->
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
                mathcedRAGrade?.let {raGrade ->
                    findRAIndex(raGrade.component.name)?.let { raIndex ->
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
    fun overwriteFile() {
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
     * @param CSVStudentAssembler The student assembler used to assemble the student data.
     * @param mainArgs The main arguments containing the necessary file paths and configurations.
     */
    fun updateCSVFile(
        students: MutableList<Student>,
        CSVStudentAssembler: CSVStudentAssembler,
        mainArgs: MainArgs,
    ) {

        updateCEGrades(students)
        updateRAGrades(students)
        overwriteFile()
        logger.debug("CSVFile updated")
    }
}