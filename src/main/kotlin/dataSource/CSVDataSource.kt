package dataSource

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


//TODO temporary... Later on filePath will be passed as Main Argument
val csvFile = getCSVFile("../files/UD1.csv")

//TODO Refactor getCSVFile to use logger instead of println
//TODO refactor getCSVFile, consider using exception railroad
fun getCSVFile(pathName: String): File {
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
