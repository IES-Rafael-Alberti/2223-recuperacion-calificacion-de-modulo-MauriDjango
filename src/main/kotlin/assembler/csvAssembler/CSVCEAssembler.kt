package assembler.csvAssembler

import assembler.Assembler
import csv.CSVUtil
import dataSource.CSVDSource
import entities.component.CEComponent
import entities.component.RAComponent
import entities.grade.*

class CSVCEAssembler(private val connection: CSVDSource): Assembler<RAGrade>() {

    override fun assemble(component: RAGrade) {

        connection.getCEComponents().forEach { ceCompData ->
            component.subComponents.find { it.component.name == ceCompData.first }?: run {
                CEComponent(ceCompData.first, CSVUtil.stringToDouble(ceCompData.second)).let { ceComp ->
                    component.subComponents.add(RAGrade(ceComp, component.id))
                }
            }
        }
    }
}