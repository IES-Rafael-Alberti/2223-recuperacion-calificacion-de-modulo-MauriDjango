package csv

import bingo.inputoutput.exceptions.log.Logging
import bingo.inputoutput.exceptions.FileEmpty
import entities.component.InstrumentComponent
import entities.grade.Grade
import entities.grade.Student
import exceptions.*
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder


//TODO temporary... Later on filePath will be passed as Main Argument


//TODO extract line iteration in methods to single function
class CSVReader(private val csvFile: File) {
    private val logger = Logging("CSVDAO")
    private var lines: List<String> = csvFile.readLines()

    init {
        if (lines.isEmpty()) throw FileEmpty
    }

    private val linesList: MutableList<MutableList<String>> = linesToList()
    private val raPercentIndex = findIndex("%RA")
    private val cePercentIndex = findIndex("%CE")?.plus(1)
    private val gradeSectionIndex = findIndex("%RA")?.plus(1)

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

    fun getCeComponents(): MutableList<Pair<String, String>> {
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

    fun getInstrumentComponents(): MutableList<Pair<String, String>> {
        val instrumentRegex = Regex("([a-z],[a-z])")
        val instruments = mutableListOf<Pair<String, String>>()

        linesList.forEach() {line ->
            line.forEach() {element ->
                instrumentRegex.find(element)?.let { match ->
                    if (raPercentIndex != null) {
                        instruments.add(
                            Pair(
                                element,
                                line[raPercentIndex]
                            )
                        )
                    } else throw RAPercentIndex
                }
            }
        }
        if (instruments.isEmpty()) {
            throw InstrumentComponentsEmpty
        }
        return instruments
    }

    fun getInstrumentGrade(studentName: String, component: InstrumentComponent): String? {
        var grade: String? = null
        val studentIndex = findIndex(studentName)
        val instrumentRegex = Regex("([a-z],[a-z])")

        linesList.forEach { line ->
            line.forEach { element ->
                instrumentRegex.find(element)?.let {
                    if (component.componentName == element) {
                        if (studentIndex != null && raPercentIndex !== null) {
                            grade = line[studentIndex]
                        }
                    }
                }
            }
        }
        if (grade == null) throw InstrumentGradeEmpty
        return grade
    }

    fun updateCEGrades(students: MutableList<Student>) {
        val ra: String? = getRAComponent()?.first
        val newLinesList: MutableList<MutableList<String>> = linesList.toMutableList()
        var mathcedRAGrade: Grade? = null

        students.forEach { student ->
            student.modulos.forEach { modulo ->
                modulo.subComponents.forEach { raGrade ->
                    if (raGrade.component.componentName == ra) {
                        mathcedRAGrade = raGrade
                    }
                }
            }

            findIndex(student.name)?.let { studentIndex ->
                mathcedRAGrade?.subComponents?.forEach { ce ->
                    findCEIndex(ce.component.componentName)?.let { ceIndex ->
                        newLinesList[ceIndex][studentIndex] = ce.getGrade().toString()
                    }
                }
            }
        }
        linesList.clear()
        linesList.addAll(newLinesList)
    }

    fun updateRAGrades(students: MutableList<Student>) {
        val ra: String? = getRAComponent()?.first
        val newLinesList: MutableList<MutableList<String>> = linesList.toMutableList()
        var mathcedRAGrade: Grade? = null

        students.forEach { student ->
            student.modulos.forEach { modulo ->
                modulo.subComponents.forEach { raGrade ->
                    if (raGrade.component.componentName == ra) {
                        mathcedRAGrade = raGrade
                    }
                }
            }

            findIndex(student.name)?.let { studentIndex ->
                mathcedRAGrade?.let {raGrade ->
                    findRAIndex(raGrade.component.componentName)?.let { raIndex ->
                        newLinesList[raIndex][studentIndex] = mathcedRAGrade?.getGrade().toString()
                    }
                }
            }
        }
        linesList.clear()
        linesList.addAll(newLinesList)
    }

    fun writeNewGrades() {
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
}