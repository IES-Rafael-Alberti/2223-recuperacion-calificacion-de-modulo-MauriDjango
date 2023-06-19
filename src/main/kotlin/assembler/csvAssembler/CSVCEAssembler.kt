package assembler.csvAssembler

import assembler.Assembler
import csv.CSVUtil
import dataSource.CSVDSource
import entities.component.CEComponent
import entities.grade.*


/**
 * CSVCEAssembler is responsible for assembling CSV data for CE grades of an RAGrade component.
 *
 * @property connection The CSVDSource connection used to fetch data.
 */
class CSVCEAssembler(private val connection: CSVDSource): Assembler<RAGrade>() {

    /**
     * Assembles CSV data for CE grades of the provided RAGrade component.
     *
     * @param component The RAGrade component to assemble CE grades for.
     */
    override fun assemble(component: RAGrade) {

        connection.getCEComponents().forEach { ceCompData ->
            component.subComponents.find { it.component.name == ceCompData.first }?: run {
                CEComponent(ceCompData.first, CSVUtil.stringToDouble(ceCompData.second)).let { ceComp ->
                    component.subComponents.add(CEGrade(ceComp, component.id))
                }
            }
        }
    }
}
