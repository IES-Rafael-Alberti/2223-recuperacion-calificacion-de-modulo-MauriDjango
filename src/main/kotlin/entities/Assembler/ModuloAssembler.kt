package entities.Assembler

import csvFile.CSVReader
import entities.component.ModuloComponent
import mainArgs

class ModuloAssembler(csvReader: CSVReader) : Assembler<ModuloComponent> {

    override fun assemble(): ModuloComponent {
        TODO("Not yet implemented")
    }

    fun assembleModulo(): ModuloComponent {
        return ModuloComponent( mainArgs.getModulo(), 100.00)
    }

}