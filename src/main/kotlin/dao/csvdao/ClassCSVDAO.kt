package bingo.inputoutput.dao.CSV

import bingo.inputoutput.dataSource.file.ModCSVFile
import entities.Modulo

class ClassCSVDAO(private val modFile: ModCSVFile): CSVDAO<Modulo>(modFile) {
    override fun create(): Modulo {
        TODO("Not yet implemented")
    }

    override fun getById(): Modulo {
        if (modFile.getStudents().isEmpty()) {
            modFile.getStudents().let{

            }
        }
    }

    override fun updateById(): Modulo {
        TODO("Not yet implemented")
    }

    override fun deleteById(): Modulo {
        TODO("Not yet implemented")
    }

}