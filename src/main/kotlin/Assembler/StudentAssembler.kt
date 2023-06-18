package Assembler

import csv.CSVHandler
import entities.component.CEComponent
import entities.component.InstrumentComponent
import entities.component.ModuloComponent
import entities.component.RAComponent
import entities.grade.*
import exceptions.StringToDoubleDefault
import exceptions.StudentEmpty


/**
 * This class inherits from Assembler so that we know what it's purpose is (To assemble)
 *
 * @param csvHandler: An object that extracts the data in a .csv file
 */
class StudentAssembler(private val csvHandler: CSVHandler): Assembler<Student>() {

    /**
     * Creates a list of Student objects for
     * each student name found in the CSVHandler that is not already in existingStudents by name
     * and the students objects that are already stored in students
     *
     * @param students: A list of students that have already been created
     * @return students: The original list passed with new students added
     */
    fun getStudents(students: MutableList<Student>, moduloName: String): MutableList<Student> {

        csvHandler.getStudents().forEach { studentName ->
            students.find { it.name == studentName }?.let { student ->
                addStudentModulos(student, moduloName)
            } ?:run {
                Student(studentName).let { student ->
                    addStudentModulos(student, moduloName)
                    students.add(student)
                }
            }
        }
        if (students.isEmpty()) throw StudentEmpty
        return students
    }

    /**
     * Attaches a new Modulo to a student
     * if the student does not already have it assigned
     *
     * @param student: A student object
     * @param moduloName: The name of the modulo
     */
    private fun addStudentModulos(student: Student, moduloName: String) {

        student.modulos.find { it.moduloName == moduloName }?.let{ modulo ->
           getRAGrades(student, modulo)
        } ?:run { newModulo(student, moduloName).let { modulo ->
                getRAGrades(student, modulo)
                student.modulos.add(modulo)
            }
        }
    }

    /**
     * Attaches new RAs to a Modulo
     * if the modulo does not already have it assigned
     *
     * @param student: A student object
     * @param moduloName: The name of the modulo being added
     */
    private fun newModulo(student: Student, moduloName: String): Modulo =
        Modulo(moduloName, ModuloComponent(moduloName), student.id)

    /**
     * Attaches new RAGrades to a Modulo
     * if the modulo does not already have it assigned
     *
     * @param student: A student object
     * @param modulo: Modulo object that belongs to student
     */
    private fun getRAGrades(student: Student, modulo: Modulo) {
        val raData = csvHandler.getRAComponent()
        val raComponent = raData?.let { RAComponent(it.first, raData.second) }

        raComponent?.let { raComp ->
            modulo.subComponents.find { it.component.componentName == raComp.componentName }?.let { raGrade ->
                getCEGrades(student, raGrade)
            } ?:run { -> raComp
                RAGrade(raComp, modulo.id).let { raGrade ->
                    modulo.subComponents.add(raGrade)
                    getCEGrades( student, raGrade)
                }
            }
        }
    }

    /**
     * Attaches new CEGrades to a RAGrade
     *
     * @param student: student object
     * @param raGrade: raGrade object that belongs to student
     */
    private fun getCEGrades(student: Student, raGrade: Grade) {

        csvHandler.getCeComponents().forEach { ceData ->
            raGrade.subComponents.find { it.component.componentName == ceData.first }?.let { ceGrade ->
                getInstrumentGrades(student, ceGrade)
            } ?:run {
                CEComponent(ceData.first, CSVUtil.stringToDouble(ceData.second)).let { ceComp ->
                    CEGrade(ceComp, raGrade.id).let { ceGrade ->
                        getInstrumentGrades(student, ceGrade)
                        raGrade.subComponents.add(ceGrade)
                    }
                }
            }
        }
    }

    /**
     * Attaches new InstrumentGrade to a CEGrade
     *
     * @param student: student object
     * @param ce: ceGrade object that belongs to student
     */
    private fun getInstrumentGrades(student: Student, ce: Grade) {

        csvHandler.getInstrumentComponents().forEach { instCompData ->
            ce.subComponents.find { it.component.componentName == instCompData.first }?.let { instGrade ->
                csvHandler.getInstrumentGrade(student.name, instGrade.component)?.let { newGrade ->
                    (instGrade as? InstrumentGrade)?.setGrade(CSVUtil.stringToDouble(newGrade))
                }
            } ?: run {
                if (ce.gradeName in instCompData.first) {
                    InstrumentComponent(instCompData.first, CSVUtil.stringToDouble(instCompData.second)).let { instComp ->
                        InstrumentGrade(instComp, grade = 0.0, ce.id).let { instGrade ->
                            csvHandler.getInstrumentGrade(student.name, instGrade.component)?.let { newGrade ->
                                (instGrade as? InstrumentGrade)?.setGrade(CSVUtil.stringToDouble(newGrade))
                            }
                            ce.subComponents.add(instGrade)
                        }
                    }
                }
            }
        }
    }
}

