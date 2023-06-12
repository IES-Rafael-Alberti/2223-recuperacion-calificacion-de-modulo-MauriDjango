package csvFile

import bingo.inputoutput.exceptions.log.Logging
import bingo.inputoutput.exceptions.FileEmpty
import dataSource.getCSVFile
import entities.component.CEComponent
import entities.component.InstrumentComponent
import entities.component.RAComponent
import entities.grade.InstrumentGrade
import entities.grade.StudentGrade
import exceptions.*
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder


//TODO temporary... Later on filePath will be passed as Main Argument
val file = getCSVFile("src/files/UD1.csv")
val csvReader = CSVReader(file)


//TODO extract line iteration in methods to single function
//TODO consider putting method returns into properties
class CSVReader(csvFile: File) {
    private var lines: List<String>
    private val logger = Logging("CSVDAO")
    val linesList: MutableList<MutableList<String>>


    init {
        lines = csvFile.readLines()
        if (lines.isEmpty()) throw FileEmpty
        linesList = linesToList()
    }

    private val raPercentIndex = findIndex("%RA")
    private val cePercentIndex = findIndex("%CE")
    private val gradeSectionIndex = linesList.indexOfFirst { it.isNotEmpty() }

/*    val raComponents: Pair<String, Double>? = getRaComponents()
    val ceComponents: MutableList<Pair<String, Double>> = getCeComponents()
    val instrumentComponents: MutableList<Pair<String, Double>> = getInstrumentComponents()

    val studentGrades: MutableList<String> = getStudentNames()
    val raGrade: MutableList<MutableList<String>> = getRAGrade()
    val ceGrades: MutableList<MutableList<String>> = getCeGrades()
    val instrumentGrades: MutableList<MutableList<String>> = getInstrumentGrades()*/


    private fun findIndex(string: String): Int? {
        var index: Int? = null

        linesList.forEach() {list ->
            list.indexOf(string).let {
                if (it > -1) {
                    index = it
                }
            }
        }
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

    private fun getStudentGrades(): MutableList<StudentGrade> {
        val studentGrades: MutableList<StudentGrade> = mutableListOf()

        getStudentNames().forEach {studentName ->
            studentGrades.add(
                StudentGrade(studentName)
            )
        }
        if (studentGrades.isEmpty()) throw StudentGradesEmpty
        return studentGrades
    }

    private fun getStudentNames(): MutableList<String> {
        val studentList = mutableListOf<String>()
        val firstStudentIndex: Int?

        firstStudentIndex = lines[0].split(',').indexOfFirst { it != "" }

        lines[1].split(',').drop(firstStudentIndex).forEach() {
            if (it != ",") studentList.add(it)
        }

        if (studentList.isEmpty()) throw StudentsNotFound
        return studentList
    }

    private fun getRAGrade(): MutableList<MutableList<String>> {
        val raRowRegex = Regex("UD(\\d+)")
        val rsGrades: MutableList<MutableList<String>> = mutableListOf()

        linesList.forEach {line ->
            line.forEach {element ->
                raRowRegex.find(element)?.let {
                    rsGrades.add(line.subList(gradeSectionIndex, line.size))
                    }
                }
            }
        if (rsGrades.isEmpty()) throw RAGradeEmpty
        return rsGrades
    }

    private fun getCeGrades(): MutableList<MutableList<String>> {
        val ceRegex = Regex("UD\\d*\\.([a-z])")
        val ceGradesList: MutableList<
                MutableList<String>> = mutableListOf()

        linesList.forEach { line ->
            line.forEach {element ->
                ceRegex.find(element)?.let { match ->
                    ceGradesList.add(line.subList(gradeSectionIndex, line.size))
                }
            }
        }
        if (ceGradesList.isEmpty()) throw CEGradesListEmpty
        return ceGradesList
    }

    private fun getInstrumentGrades(): MutableList<MutableList<String>> {
        val ceGroupRegex = Regex("(([a-z],)+[a-z]).+,(\\d+)%,")
        val instrumentGrades: MutableList<MutableList<String>> = mutableListOf()

        linesList.forEach {line ->
            line.forEach {element ->
                ceGroupRegex.find(element)?.let {
                    instrumentGrades.add(line.subList(gradeSectionIndex, line.size))
                }
            }
        }
        if (instrumentGrades.isEmpty()) throw InstrumentGradeEmpty
        return instrumentGrades
    }

    private fun getRaComponents(): Pair<String, Double>? {
        val raRegex = Regex("RA(\\d)")
        var ra: Pair<String, Double>? = null

        linesList.forEach { line ->
            line.forEach { element ->
                raRegex.find(element)?.let { matchResult ->
                    if (raPercentIndex != null) {
                        ra = Pair(matchResult.groupValues[1], line[raPercentIndex].toDouble())
                    }
                }
            }
        }
        if (ra == null) throw NoRAFound
        return ra
    }


    private fun getCeComponents(): MutableList<Pair<String, Double>> {
        val ceRegex = Regex("UD\\d*\\.([a-z])")
        val ceList: MutableList<Pair<String, Double>> = mutableListOf()

        try {
            linesList.forEach() { line ->
                line.forEach() { element ->
                    ceRegex.find(element)?.let {
                        if (cePercentIndex != null) {
                            ceList.add(
                                Pair(
                                    it.groupValues[1],
                                    line[cePercentIndex].toDouble()
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

    fun getInstrumentComponents(): MutableList<Pair<String, Double>> {
        val ceGroupRegex = Regex("(([a-z],)+[a-z]).+,(\\d+)%,")
        val instruments = mutableListOf<Pair<String, Double>>()

        linesList.forEach() {line ->
            line.forEach() {element ->
                ceGroupRegex.find(element)?.let { match ->
                    if (raPercentIndex != null) {
                        instruments.add(
                            Pair(
                                match.groupValues[1],
                                line[raPercentIndex].toDouble()
                            )
                        )
                    } else throw RAPercentIndex
                }
            }
        }
        return instruments
    }
}
