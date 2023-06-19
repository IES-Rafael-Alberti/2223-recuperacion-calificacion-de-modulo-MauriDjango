package assembler.csvAssembler

import assembler.Assembler
import csv.CSVUtil
import dataSource.CSVDSource
import entities.component.InstrumentComponent
import entities.grade.*


/**
 * CSVInstrumentAssembler is responsible for assembling CSV data for instrument grades of a CEGrade component.
 *
 * @property connection The CSVDSource connection used to fetch data.
 */
class CSVInstrumentAssembler(private val connection: CSVDSource): Assembler<CEGrade>() {

    /**
     * Assembles CSV data for instrument grades of the provided CEGrade component.
     *
     * @param component The CEGrade component to assemble instrument grades for.
     */
    override fun assemble(component: CEGrade) {
        connection.getInstrumentComponents().forEach { instCompData ->
            component.subComponents.find { it.component.name == instCompData.first } ?: run {
                //Extra if conditional to check if the instrument belongs in CE
                if (component.gradeName in instCompData.first) {
                    InstrumentComponent(
                        instCompData.first,
                        CSVUtil.stringToDouble(instCompData.second)
                    ).let { instComp ->
                        InstrumentGrade(instComp, grade = 0.0, component.id).let { instGrade ->
                            component.subComponents.add(instGrade)
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the grade for the specified student and instrument component.
     *
     * @param studentName The name of the student.
     * @param component The InstrumentGrade component to retrieve the grade for.
     */
    fun getGrade(studentName: String, component: InstrumentGrade) {
        connection.getInstrumentGrade(studentName, component.component)?.let { grade ->
            component.setGrade(CSVUtil.stringToDouble(grade))
        }
    }
}
