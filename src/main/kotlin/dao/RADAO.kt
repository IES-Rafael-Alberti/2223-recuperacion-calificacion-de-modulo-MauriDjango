package dao

import entities.component.ModuloComponent
import entities.component.RAComponent
import entities.grade.Grade
import entities.grade.Modulo
import entities.grade.RAGrade
import exceptions.StudentEmpty
import hikarih2ds
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource


val raDAO = RADAO(hikarih2ds)

/**
 * A class that implements DAO
 * Accesses the Instrument aspects of the DB
 *
 * @param dataSource: A datasource that provides the connection to the database
 * @property logger: Logger
 */
class RADAO(private val dataSource: DataSource): DAO<Grade> {
    private val logger = LoggerFactory.getLogger("RADAO")

    override fun create(t: Grade): Grade {
        try {
            val sql = "INSERT INTO RESULTADOAPRENDIZAJE (id, raName, moduloID, grade, percentage) VALUES (?, ?, ?, ?, ?)"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.component.componentName)
                    stmt.setString(3, t.superComponentID.toString())
                    stmt.setDouble(4, t.getGrade())
                    stmt.setDouble(5, t.component.percentage)
                    stmt.execute()
                }
            }
        } catch (e: SQLException) {
            logger.warn(e.message)
        }
        return t
    }

    override fun createTable() {
        try {
            val sql =
                "CREATE TABLE RESULTADOAPRENDIZAJE (id VARCHAR(50) PRIMARY KEY, raName VARCHAR(50), moduloID VARCHAR(50), grade DOUBLE, percentage DOUBLE);"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val result = stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            logger.warn(e.message)
        }
    }

    override fun deleteById(t: Grade): Grade {
        val sql = "DELETE FROM RESULTADOAPRENDIZAJE WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                stmt.executeUpdate()
            }
        }
        return t
    }

    override fun deleteAll() {
        val sql = "DELETE FROM RESULTADOAPRENDIZAJE"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    override fun getAll(): MutableList<Grade> {
        val sql = "SELECT * FROM RESULTADOAPRENDIZAJE"
        val ra: MutableList<Grade> = mutableListOf()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    ra.add(
                        RAGrade(
                            component = RAComponent(
                                resultSet.getString("raName"),
                                resultSet.getDouble("percentage")
                            ),
                            superComponentID = UUID.fromString(resultSet.getString("moduloID")),
                            id = UUID.fromString(resultSet.getString("id"))
                        )
                    )
                }
            }
        }
        return ra
    }

    override fun updateById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

    override fun getById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

}