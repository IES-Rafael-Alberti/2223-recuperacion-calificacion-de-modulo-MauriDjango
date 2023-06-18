package csv

import exceptions.StringToDoubleDefault

object CSVUtil {
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