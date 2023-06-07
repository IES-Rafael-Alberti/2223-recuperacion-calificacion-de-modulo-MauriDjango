package entities

import java.util.*

class Instrumentos {
}

data class InstrumentosData(
    var id: UUID = UUID.randomUUID(),
    var instrQual: String
)