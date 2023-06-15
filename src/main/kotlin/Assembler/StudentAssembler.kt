package Assembler

import entities.grade.Student
import csv.CSVReader
import entities.grade.Modulo
import entities.component.CEComponent
import entities.component.InstrumentComponent
import entities.component.ModuloComponent
import entities.component.RAComponent
import entities.grade.CEGrade
import entities.grade.InstrumentGrade
import entities.grade.RAGrade
import exceptions.CEGradesEmpty
import exceptions.GetInstrumentGradesEmpty
import exceptions.StudentEmpty

class StudentAssembler(private val csvReader: CSVReader): Assembler<Student>(csvReader) {

    fun getStudents(students: MutableList<Student>): MutableList<Student> {
        val existingStudents = students.map { it.name }

        csvReader.getStudents().forEach { studentName ->
            if (studentName !in existingStudents) {
                students.add(Student(studentName))
            }
        }
        if (students.isEmpty()) throw StudentEmpty
        return students
    }

    fun addStudentModulos(student: Student, moduloName: String) {
        val modulos = student.modulos.map { it.component.componentName }

        if (moduloName !in modulos) {
            student.modulos.add(getStudentModulo(student, moduloName))
        }
    }

    private fun getStudentModulo(student: Student, moduloName: String): Modulo =
        Modulo(moduloName, ModuloComponent(moduloName), student.id).apply {
            val moduloRAs = this.subComponents.map { it.component.componentName }

            getRAGrades(student, this)?.let {raGrade ->
                if (raGrade.component.componentName !in moduloRAs)
                this.subComponents.add(raGrade) }
        }

    private fun getRAGrades(student: Student, modulo: Modulo): RAGrade? {
        val raData = csvReader.getRAComponent()
        if (raData?.second == "") {
            println()
        }
        val raComp = raData?.let { RAComponent(it.first, stringToDouble(raData.second)) }
        var raGrade: RAGrade? = null

        if (raComp != null) {
            raGrade = RAGrade(raComp, modulo.id).apply {
                this.subComponents.addAll(getCEGrades(student, this))
            }
        }
        return raGrade
    }

    private fun getCEGrades(student: Student, raGrade: RAGrade): MutableList<CEGrade> {
        val ceList: MutableList<CEGrade> = mutableListOf()

        csvReader.getCeComponents().forEach { ceData ->
            ceList.add(
                CEGrade(
                    CEComponent(ceData.first, stringToDouble(ceData.second)),
                    raGrade.id
                ).apply {
                    this.subComponents.addAll(getInstrumentGrades(student, this))
                }
            )
        }
        if (ceList.isEmpty()) throw CEGradesEmpty
        return ceList
    }

    private fun getInstrumentGrades(student: Student, ce: CEGrade): MutableList<InstrumentGrade> {
        val instList: MutableList<InstrumentGrade> = mutableListOf()

        csvReader.getInstrumentComponents().forEach { instCompData ->
            if (ce.component.componentName in instCompData.first) {
                val instrumentComponent = InstrumentComponent(instCompData.first, stringToDouble(instCompData.second))
                val instGrade = csvReader.getInstrumentGrade(student.name, instrumentComponent)
                if (instGrade != null) {
                    instList.add(
                        InstrumentGrade(
                            instrumentComponent,
                            stringToDouble(instGrade),
                            ce.id
                        )
                    )
                }
            }
        }
        if (instList.isEmpty()) throw GetInstrumentGradesEmpty
        return instList
    }
}

