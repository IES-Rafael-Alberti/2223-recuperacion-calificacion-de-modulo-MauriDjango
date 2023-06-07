package bingo.inputoutput.dao.DB

import entities.Modulo
import javax.sql.DataSource

class ClassDBDAO(ds: DataSource): DBDAO<Modulo>(ds) {
    override fun create(): Modulo {
        TODO("Not yet implemented")
    }

    override fun getById(): Modulo {
        TODO("Not yet implemented")
    }

    override fun updateById(): Modulo {
        TODO("Not yet implemented")
    }

    override fun deleteById(): Modulo {
        TODO("Not yet implemented")
    }

}