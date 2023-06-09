import csvFile.csvReader
import dto.ModuloDTO

fun main() {
    val moduloDTO = ModuloDTO.assemble("Prog", csvReader)
}