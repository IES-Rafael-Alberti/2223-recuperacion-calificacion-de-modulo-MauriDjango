package csv

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


//TODO Refactor getCSVFile to use logger instead of println
//TODO refactor getCSVFile, consider using exception railroad
fun getCSVFiles(pathName: String): MutableList<File> {
    val directoryPath = File(pathName)
    val csvFileList: MutableList<File> = mutableListOf()

    try {
        directoryPath.walk().forEach { file ->
            if (file.isFile && file.extension == "csv") {
                csvFileList.add(file)
            }
        }
    }
    catch (e: FileNotFoundException) {
        println("File not found in getCSVFile")
        throw e
    }
    catch (e: IOException) {
        println("Exception thrown in getCSVFile")
        throw e
    }
    return csvFileList
}