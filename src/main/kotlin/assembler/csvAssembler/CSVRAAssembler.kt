package assembler.csvAssembler

import assembler.Assembler
import csv.CSVUtil
import dataSource.CSVDSource
import entities.component.ModuloComponent
import entities.component.RAComponent
import entities.grade.Modulo
import entities.grade.RAGrade

class CSVRAAssemblerStudent(private val connection: CSVDSource): Assembler<Modulo>() {

    override fun assemble(component: Modulo) {

        connection.getRAComponent()?.let { raCompData ->
            component.subComponents.find { it.component.name == raCompData.first } ?: run {
                RAComponent(raCompData.first, CSVUtil.stringToDouble(raCompData.second)).let { raComp ->
                        component.subComponents.add(RAGrade(raComp, component.id))
                }
            }
        }
    }
}