package entities.grade

import java.util.*


/**
 * Represents a student.
 *
 * @param name The name of the student.
 * @param id The unique ID of the student (default value is generated using [UUID.randomUUID()]).
 */
class Student(var name: String,val initials: String, var id: UUID = UUID.randomUUID()) {

    val modulos: MutableList<Modulo> = mutableListOf()
}
