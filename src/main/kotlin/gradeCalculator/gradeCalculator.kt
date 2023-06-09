package gradeCalculator

import csvFile.CSVReader

class GradeCalculator(val moduloCSVFile: CSVReader) {
    private val ce = moduloCSVFile.getCEs()
    private val instruments = moduloCSVFile.getInstruments()
    private val raPercentageIndex: Int? = moduloCSVFile.findIndex("%RA")
    private val linesList: MutableList<MutableList<String>> = moduloCSVFile.linesList

    fun calcCEGrade(grade: Float, percentRA: Int) {
        instruments.forEach() { instrument ->

        }
    }
}