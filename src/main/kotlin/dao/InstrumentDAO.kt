package dao

import entities.component.InstrumentComponent
import entities.grade.Grade
import entities.grade.InstrumentGrade
import hikarih2ds
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource


val instrumentDAO = InstrumentDAO(hikarih2ds)

/**
 * A class that implements DAO
 * Accesses the Instrument aspects of the DB
 *
 * @param dataSource: A datasource that provides the connection to the database
 * @property logger: Logger
 */
class InstrumentDAO(private val dataSource: DataSource): DAO<Grade> {
    private val logger = LoggerFactory.getLogger("InstrumentDAO")

    override fun create(t: Grade): Grade {
        try {
            val sql = "INSERT INTO INSTRUMENT (id, instrumentName, ceID, grade, percentage) VALUES (?, ?, ?, ?, ?)"
             dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.component.name)
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
                "CREATE TABLE INSTRUMENT (id VARCHAR(50) PRIMARY KEY, instrumentName VARCHAR(50), ceID VARCHAR(50), grade DOUBLE, percentage DOUBLE);"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val result = stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            logger.warn(e.message)
        }
    }

    override fun getById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

    override fun updateById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

    override fun deleteById(t: Grade): Grade {
        val sql = "DELETE FROM INSTRUMENT WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                stmt.executeUpdate()
            }
        }
        return t
    }

    override fun deleteAll() {
        val sql = "DELETE FROM INSTRUMENT"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    override fun getAll(): MutableList<Grade> {
        val sql = "SELECT * FROM INSTRUMENT"
        val instruments: MutableList<Grade> = mutableListOf()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    instruments.add(
                        InstrumentGrade(
                            component = InstrumentComponent(
                                resultSet.getString("instrumentName"),
                                resultSet.getDouble("percentage")
                            ),
                            superComponentID = UUID.fromString(resultSet.getString("ceID")),
                            id = UUID.fromString(resultSet.getString("id")),
                            grade = resultSet.getDouble("grade")
                        )
                    )
                }
            }
        }
        return instruments
    }
}