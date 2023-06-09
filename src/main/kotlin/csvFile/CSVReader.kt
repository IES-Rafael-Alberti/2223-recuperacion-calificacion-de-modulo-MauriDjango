package csvFile

import bingo.inputoutput.exceptions.log.Logging
import bingo.inputoutput.exceptions.FileEmpty
import dataSource.getCSVFile
import entities.component.Instrument
import exceptions.*
import java.io.File
import java.lang.StringBuilder


//TODO temporary... Later on filePath will be passed as Main Argument
val file = getCSVFile("src/files/UD1.csv")
val csvReader = CSVReader(file)


class CSVReader(csvFile: File) {
    private var lines: List<String>
    private val logger = Logging("CSVDAO")
    val linesList: MutableList<MutableList<String>>

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

    fun getRA(): Int? {
        val raRegex = Regex("RA(\\d)")
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

    fun getInstruments(): Pair<String, Int>? {
        val ceGroupRegex = Regex("(([a-z],)+[a-z]).+,(\\d+)%,")
        var instrumentPair: Pair<String, Int>? = null
        var group: String?
        var percent: Int?

        lines.forEach() { line ->
            val match = ceGroupRegex.find(line)
            group = match?.groupValues?.get(1)
            percent = match?.groupValues?.get(3)?.toInt()

            // Doing a null check before non-null assertion
            if (group != null && percent != null) {
                instrumentPair = Pair(group!!, percent!!)
            } else {
                 throw InstrumentsNotFound
            }
        }
        return instrumentPair
    }

    fun getCEs(): List<String> {
        val ceRegex = Regex("UD\\d*\\.([a-z])")
        val ceList = mutableListOf<String>()

        lines.forEach() { line ->
            val match = ceRegex.find(line)
            match?.groupValues?.get(1)?.let { it1 -> ceList.add(it1) }
        }

        if (ceList.isEmpty()) throw CENotFound
        return ceList
    }

    //TODO Check function of get grade section
    fun getGradeSection(): MutableList<MutableList<String>> {
        val lineListCopy = linesList.toMutableList()
        val firstStudentIndex: Int? = getStudentIndex(getStudents()[0])

        lineListCopy.forEach { line ->
            firstStudentIndex?.let { lineListCopy.drop(it) }
            lineListCopy.add(line)
        }

        if (lineListCopy.isEmpty()) throw GradeSectionError
        return lineListCopy
    }

    private fun getStudentIndex(student: String): Int? {
        val columnStartIndex: Int?

        linesList[1].indexOf(student).let { index ->
            if (index > -1) {
                columnStartIndex = index
            } else {
                throw StudentIndexNotFound
            }
        }
        return columnStartIndex
    }

    fun findIndex(string: String): Int? {
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

    fun findInstrumentRow(instrument: String): Int {
        var instrumentRow: Int = -1

         linesList.forEach { line ->
                if (line.contains(instrument)) {
                    instrumentRow = line.indexOf(instrument)
                }
            }
        if (instrumentRow < 0) throw InstrumentRowNotFound

        return instrumentRow
    }
}
