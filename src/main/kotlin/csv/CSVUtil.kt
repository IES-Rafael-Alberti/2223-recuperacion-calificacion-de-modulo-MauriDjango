package csv

import exceptions.StringToDoubleDefault


/**
 * Utility object for CSV-related operations.
 */
object CSVUtil {

    /**
     * Converts a string to a double value.
     *
     * @param string The string to convert.
     * @return The double value.
     * @throws StringToDoubleDefault if the conversion fails and no default value is provided.
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
