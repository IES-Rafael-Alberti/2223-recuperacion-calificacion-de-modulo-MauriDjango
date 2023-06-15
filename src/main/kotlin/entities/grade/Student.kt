package entities.grade

import java.util.*


class Student(var name: String, var id: UUID = UUID.randomUUID()) {
    val initials: String = ""

    val modulos: MutableList<Modulo> = mutableListOf()
}