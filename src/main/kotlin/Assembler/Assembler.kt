package Assembler

import csv.CSVReader
import exceptions.StringToDoubleDefault

/**
 * This class helps with the assembly of multiple classes that are comprised of each other
 *
 */
abstract class Assembler<T>(csvReader: CSVReader) {
    fun stringToDouble(string: String): Double {
        val percentRegex = Regex("(\\d+,?\\d*)")
        var double: Double? = null

        percentRegex.find(string)?.let { match ->
            double = match.groupValues[1].replace(',','.').toDouble()
        }

        if (double == null) {
            double = 0.00
            throw StringToDoubleDefault
        }
        return double as Double
    }
}
