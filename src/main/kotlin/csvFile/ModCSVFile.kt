package csvFile

import bingo.inputoutput.exceptions.log.Logging
import bingo.inputoutput.exceptions.FileEmpty
import exceptions.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.StringBuilder


//TODO temporary... Later on filePath will be passed as Main Argument
val file = getCSVFile("src/files/UD1.csv")
val modCsvFile = ModCSVFile(file)


class ModCSVFile(csvFile: File){
    private var lines: List<String>
    private val logger = Logging("CSVDAO")
    private val linesList: MutableList<MutableList<String>>

    init {
        lines = csvFile.readLines()
        if (lines.isEmpty()) throw FileEmpty
        linesList = linesToList()
    }

    private fun linesToList(): MutableList<MutableList<String>> {
        val allLines: MutableList<MutableList<String>> = mutableListOf()
        val lineListed: MutableList<String> = mutableListOf()
        val string = StringBuilder()
        var quotesOpen = false

        lines.forEach() { line ->
            line.forEach { char ->

                if (char == ',' && !quotesOpen)
                {
                    lineListed.add(string.toString())
                    string.clear()
                }
                else if (char == '"')
                {
                    quotesOpen = !quotesOpen
                }
                else
                    string.append(char)
            }
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

    fun getRA(): Int? {
        val raRegex = Regex("RA(\\d{1})")
        var ra: Int? = null

        lines.forEach { line ->
            val match = raRegex.find(line)

            match?.groupValues?.get(1)?.let {
                if (ra != null && ra != it.toInt()) throw RADetectedTwice
                ra = it.toInt()
            }
        }
        if (ra == null) throw NoRAFound

        return ra
    }

    fun getCEGroups(): List<String> {
        val ceGroupRegex = Regex("([a-z],)+[a-z]")
        val ceGroupList = mutableListOf<String>()

        lines.forEach() { line ->
            val match = ceGroupRegex.find(line)
            match?.let { matchResult -> ceGroupList.add(matchResult.value) }
        }

        if (ceGroupList.isEmpty()) throw CENotFound
        return ceGroupList
    }

    fun getCEs(): List<String> {
        val ceRegex = Regex("UD\\d*\\.([a-z])")
        val ceList = mutableListOf<String>()

        lines.forEach() {line ->
            val match = ceRegex.find(line)
            match?.groupValues?.get(1)?.let { it1 -> ceList.add(it1) }
        }

        if (ceList.isEmpty()) throw CENotFound
        return ceList
    }

    fun getGradeSection(): MutableList<MutableList<String>> {
        val lineListCopy = linesList.toMutableList()
        val firstStudentIndex: Int? = getStudentIndex(getStudents()[0])

            lineListCopy.forEach {line ->
                firstStudentIndex?.let { lineListCopy.drop(it)}
                lineListCopy.add(line)
            }

        if (lineListCopy.isEmpty()) throw GradeSectionError
        return lineListCopy
    }

    private fun getStudentIndex(student: String): Int? {
        val columnStartIndex: Int?

        linesList[1].indexOf(student).let {index ->
            if (index > -1) {
                columnStartIndex = index
            }
            else {
                throw StudentIndexNotFound
            }
        }
        return columnStartIndex
    }
}

    val decimalRegex = Regex(",\"(\\d,?\\d+)\",")

    fun getFinalGradesUD() {
        //TODO Not yet implemented
    }

    fun getInstrumentos() {
        //TODO Not yet implemented
    }

    fun getFinalsGradesCE() {
        //TODO Not yet implemented
    }


    fun getUnidad() {
        //TODO Not yet implemented
    }

//TODO Refactor getCSVFile to use logger instead of println
//TODO refactor getCSVFile, consider using exception railroad
fun getCSVFile(pathName: String): File  {
    val csvFile: File?

    try {
        csvFile = File(pathName)
    }
    catch (e: FileNotFoundException) {
        println("File not found in getCSVFile")
        throw e
    }
    catch (e: IOException) {
        println("Exception thrown in getCSVFile")
        throw e
    }
        return csvFile
}
