package assembler.csvAssembler

import assembler.Assembler
import entities.component.ModuloComponent
import entities.grade.Modulo
import entities.grade.Student


/**
 * CSVModuloAssembler is responsible for assembling CSV data for a specific modulo of a Student.
 *
 * @property moduloName The name of the modulo to assemble.
 */
class CSVModuloAssembler(private val moduloName: String): Assembler<Student>() {

    /**
     * Assembles CSV data for the specified modulo of the provided Student.
     *
     * @param component The Student to assemble the modulo for.
     */
    override fun assemble(component: Student) {
        component.modulos.find { it.moduloName == moduloName }?:run {
            Modulo(moduloName, ModuloComponent(moduloName), component.id).let { modulo ->
                component.modulos.add(modulo)
            }
        }
    }
}
