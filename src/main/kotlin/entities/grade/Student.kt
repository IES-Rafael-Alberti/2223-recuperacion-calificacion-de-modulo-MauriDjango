package entities.grade

import java.util.*


/**
 * Represents a student.
 *
 * @param name The name of the student.
 * @param id The unique ID of the student (default value is generated using [UUID.randomUUID()]).
 */
class Student(var name: String, var id: UUID = UUID.randomUUID()) {
    val initials: String = ""
    val modulos: MutableList<Modulo> = mutableListOf()
}