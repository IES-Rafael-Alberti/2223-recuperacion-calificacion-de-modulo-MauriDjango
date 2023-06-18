package assembler.csvAssembler

import assembler.Assembler
import dataSource.CSVDSource
import entities.component.ModuloComponent
import entities.grade.Grade
import entities.grade.Modulo
import entities.grade.Student


class CSVModuloAssembler(private val moduloName: String): Assembler<Student>() {

    override fun assemble(component: Student) {
        component.modulos.find { it.moduloName == moduloName }?:run {
            Modulo(moduloName, ModuloComponent(moduloName), component.id).let { modulo ->
                component.modulos.add(modulo)
            }
        }
    }
}
