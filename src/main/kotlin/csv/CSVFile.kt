package csv

import exceptions.NOCSVFilesFound
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


/**
 * Creates a file from a path given as a string
 *
 * @param pathName: A path as a string
 * @return csvFileList: A list containing .csv files in the directory path
 */
fun getCSVFiles(pathName: String): MutableList<File> {
    val directoryPath = File(pathName)
    val csvFileList: MutableList<File> = mutableListOf()

    try {
        directoryPath.walk().forEach { file ->
            if (file.isFile && file.extension == "csv") {
                csvFileList.add(file)
            }
        }
        if (csvFileList.isEmpty()) throw NOCSVFilesFound
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
