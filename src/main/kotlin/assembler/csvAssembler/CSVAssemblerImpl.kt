package assembler.csvAssembler

import dataSource.CSVDSource
import entities.grade.CEGrade
import entities.grade.InstrumentGrade
import entities.grade.RAGrade
import entities.grade.Student


class CSVAssemblerImpl(private val connection: CSVDSource, private val moduloName: String, private val students: MutableList<Student>) {
    private val csvStudentAssembler = CSVStudentAssembler(connection)
    private val csvModuloAssembler = CSVModuloAssembler(moduloName)
    private val csvRAAssembler = CSVRAAssemblerStudent(connection)
    private val csvCEAssembler = CSVCEAssembler(connection)
    private val csvInstrumentAssembler = CSVInstrumentAssembler(connection)

    fun assembleAll() {
        csvStudentAssembler.updateStudents(students)
        students.forEach { student ->
            csvModuloAssembler.assemble(student)
            student.modulos.forEach { modulo ->
                csvRAAssembler.assemble(modulo)
                modulo.subComponents.forEach { raGrade ->
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