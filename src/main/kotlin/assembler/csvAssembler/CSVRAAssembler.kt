package assembler.csvAssembler

import assembler.Assembler
import dataSource.CSVDSource
import entities.component.RAComponent
import entities.grade.Modulo
import entities.grade.RAGrade


/**
 * CSVRAAssemblerStudent is responsible for assembling CSV data for the RA (Responsible Authority) component of a Modulo.
 *
 * @property connection The CSVDSource connection for retrieving CSV data.
 */
class CSVRAAssembler(private val connection: CSVDSource): Assembler<Modulo>() {

    /**
     * Assembles CSV data for the RA component of the specified Modulo.
     *
     * @param component The Modulo to assemble the RA component for.
     */
    override fun assemble(component: Modulo) {

        connection.getRAComponent()?.let { raCompData ->
            component.subComponents.find { it.component.name == raCompData.first } ?: run {
                RAComponent(raCompData.first, raCompData.second).let { raComp ->
                        component.subComponents.add(RAGrade(raComp, component.id))
                }
            }
        }
    }
}
