package entities.grade

import entities.component.Component

class StudentGrade(val name:String) {
    val subComponent: MutableList<ModuloGrade> = mutableListOf()
}