package bingo.inputoutput.dao.CSV

import dao.DAO
import bingo.inputoutput.dataSource.file.ModCSVFile

val capitalLetterRegex = Regex("[A-Z]")

abstract class CSVDAO<T>(private val modCsvFile: ModCSVFile): DAO<T> {

}

