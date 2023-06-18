package assembler.csvAssembler

import dataSource.CSVDSource
import entities.grade.*


/**
 * Implementation of the CSVAssembler interface for assembling CSV data.
 *
 * @property connection the CSV data source connection.
 * @property moduloName the name of the modulo to assemble.
 * @property students the list of students to assemble.
 */
class CSVAssemblerImpl(
    private val connection: CSVDSource,
    private val moduloName: String,
    private val students: MutableList<Student>) {
    private val csvStudentAssembler = CSVStudentAssembler(connection)
    private val csvModuloAssembler = CSVModuloAssembler(moduloName)
    private val csvRAAssembler = CSVRAAssemblerStudent(connection)
    private val csvCEAssembler = CSVCEAssembler(connection)
    private val csvInstrumentAssembler = CSVInstrumentAssembler(connection)

    /**
     * Assembles all the CSV data.
     */
    fun assembleAll() {
        csvStudentAssembler.updateStudents(students)
        students.forEach { student ->
            csvModuloAssembler.assemble(student)
            student.modulos.find { it.moduloName == moduloName }?.let {modulo ->
                csvRAAssembler.assemble(modulo)
                    modulo.subComponents.find {it.component.name == connection.getRAComponent()?.first }?.let {raGrade ->
                    csvCEAssembler.assemble(raGrade as RAGrade)
                    raGrade.subComponents.forEach { ceGrade ->
                        csvInstrumentAssembler.assemble(ceGrade as CEGrade)
                        ceGrade.subComponents.forEach { instrumentGrade ->
                            csvInstrumentAssembler.getGrade(student.name, instrumentGrade as InstrumentGrade)
                        }
                    }
                }
            }
        }
    }
}
