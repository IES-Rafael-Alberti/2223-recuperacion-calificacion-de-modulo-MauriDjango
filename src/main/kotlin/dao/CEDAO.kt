package dao

import entities.component.CEComponent
import entities.grade.CEGrade
import entities.grade.Grade
import exceptions.StudentEmpty
import hikarih2ds
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource


val ceDAO = CEDAO(hikarih2ds)

class CEDAO(private val dataSource: DataSource): DAO<Grade> {
    private val logger = LoggerFactory.getLogger("CEDAO")
    override fun create(t: Grade): Grade {
        try {
            val sql = "INSERT INTO CRITERIOEVALUACION (id, ceName, raID, grade, percentage) VALUES (?, ?, ?, ?, ?)"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.gradeName)
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
                "CREATE TABLE CRITERIOEVALUACION (id VARCHAR(50) PRIMARY KEY, ceName VARCHAR(50), raID VARCHAR(50), grade DOUBLE, percentage DOUBLE);"
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
        val sql = "DELETE FROM CRITERIOEVALUACION WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                stmt.executeUpdate()
            }
        }
        return t
    }

    override fun deleteAll() {
        val sql = "DELETE FROM CRITERIOEVALUACION"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    override fun getAll(): MutableList<Grade> {
        val sql = "SELECT * FROM CRITERIOEVALUACION"
        val ce: MutableList<Grade> = mutableListOf()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    ce.add(
                        CEGrade(
                            component = CEComponent(
                                resultSet.getString("ceName"),
                                resultSet.getDouble("percentage")
                            ),
                            superComponentID = UUID.fromString(resultSet.getString("raID")),
                            id = UUID.fromString(resultSet.getString("id"))
                        )
                    )
                }
            }
        }
        if (ce.isEmpty()) throw StudentEmpty
        return ce
    }

    override fun updateById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

    override fun getById(t: Grade): Grade {
        TODO("Not yet implemented")
    }
}