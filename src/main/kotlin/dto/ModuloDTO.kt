package dto

import csvFile.CSVReader
import bingo.inputoutput.exceptions.log.Logging
import csvFile.csvReader
import entities.component.Instrument
import exceptions.DTOAssemblyError
import java.lang.Exception

val logger = Logging("ModDTO")
val moduloDTO = ModuloDTO.assemble("Prog", csvReader)

class ModuloDTO(
    val modulo: String,
    val students: List<String>,
    val ra: Int?,
    val instruments: MutableList<Instrument>,
    val ce: List<String>,
    val gradeSection: List<List<String>>
): DTO() {
    companion object{
        fun assemble(modulo: String, csvReader: CSVReader): ModuloDTO {
            return try {
                ModuloDTO(
                    modulo = modulo,
                    students = csvReader.getStudents(),
                    ra = csvReader.getRA(),
                    instruments = csvReader.getInstruments(),
                    ce = csvReader.getCEs(),
                    gradeSection = csvReader.getGradeSection()
                )
            } catch (e: Exception)
            {
                logger.warnLog(e.message)
                throw DTOAssemblyError
            }
        }
    }
}





