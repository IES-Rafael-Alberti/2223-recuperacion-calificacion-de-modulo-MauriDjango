import csvFile.modCsvFile
import dto.ModDTO

fun main() {
    val modDTO = ModDTO.assemble("Prog", modCsvFile)
}