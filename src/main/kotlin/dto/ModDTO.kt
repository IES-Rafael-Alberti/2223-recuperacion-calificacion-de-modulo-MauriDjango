package dto

import bingo.inputoutput.dataSource.file.ModCSVFile
import bingo.inputoutput.exceptions.log.Logging
import exceptions.DTOAssemblyError
import java.lang.Exception

val logger = Logging("ModDTO")

class ModDTO(
    val modulo: String,
    val students: List<String>,
    val ra: Int?,
    val ceGroup: List<String>,
    val ce: List<String>,
    val gradeSection: List<List<String>>
): DTO()

object AssembleModDTO {
    fun assemble(modulo: String, modCSVFile: ModCSVFile): ModDTO {
        return try {
            ModDTO(
                modulo = modulo,
                students = modCSVFile.getStudents(),
                ra = modCSVFile.getRA(),
                ceGroup = modCSVFile.getCEGroups(),
                ce = modCSVFile.getCEs(),
                gradeSection = modCSVFile.getGradeSection()
            )
        } catch (e: Exception) {
            logger.warnLog(e.message)
            throw DTOAssemblyError
        }
    }
}

