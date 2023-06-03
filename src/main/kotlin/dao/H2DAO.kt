package dao

import h2DS
import javax.sql.DataSource

class H2DAO<T>(ds: DataSource): DAO<T> {

    override fun create(): T {
        TODO("Not yet implemented")
    }

    override fun getById(): T {
        TODO("Not yet implemented")
    }

    override fun updateById(): T {
        TODO("Not yet implemented")
    }

    override fun deleteById(): T {
        TODO("Not yet implemented")
    }
}
