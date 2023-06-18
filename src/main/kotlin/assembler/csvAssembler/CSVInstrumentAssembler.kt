package assembler.csvAssembler

import assembler.Assembler
import csv.CSVUtil
import dataSource.CSVDSource
import entities.component.InstrumentComponent
import entities.grade.*

class CSVInstrumentAssembler(private val connection: CSVDSource): Assembler<CEGrade>() {

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

    fun getGrade(studentName: String, component: InstrumentGrade) {
        connection.getInstrumentGrade(studentName, component.component)?.let { grade ->
            component.setGrade(CSVUtil.stringToDouble(grade))
        }
    }
}