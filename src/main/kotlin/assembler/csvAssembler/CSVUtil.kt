package assembler.csvAssembler

import exceptions.StringToDoubleDefault


/**
 * Utility class for CSV-related operations.
 */
object CSVUtil {

    /**
     * Converts a string representation of a number to a double.
     *
     * The string can contain numeric characters and an optional comma for thousands separator.
     * The conversion process includes removing commas and parsing the string to a double value.
     *
     * @param string The string representation of the number.
     * @return The parsed double value.
     * @throws StringToDoubleDefault if the string is invalid or cannot be converted to a double.
     */
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