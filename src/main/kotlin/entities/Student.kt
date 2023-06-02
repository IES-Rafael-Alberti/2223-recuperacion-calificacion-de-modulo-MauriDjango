package entities

import java.util.UUID

data class Student(
    var id: UUID = UUID.randomUUID()
)