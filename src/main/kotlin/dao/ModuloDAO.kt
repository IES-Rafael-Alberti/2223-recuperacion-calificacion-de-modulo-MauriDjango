package dao

import entities.grade.Grade
import entities.grade.Modulo
import entities.component.ModuloComponent
import exceptions.StudentEmpty
import hikarih2ds
import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource


val moduloDAO = ModuloDAO(hikarih2ds)
class ModuloDAO(private val dataSource: DataSource): DAO<Grade> {
    private val logger = LoggerFactory.getLogger("ModuloDAO")

    override fun create(t: Grade): Grade {
        try {
            val sql = "INSERT INTO MODULO (id, moduloName, percentage, studentID, grade) VALUES (?, ?, ?, ?, ?)"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.gradeName)
                    stmt.setDouble(3, t.component.percentage)
                    stmt.setString(4, t.superComponentID.toString())
                    stmt.setDouble(5, t.getGrade())
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
                "CREATE TABLE MODULO (id VARCHAR(50) PRIMARY KEY, moduloName VARCHAR(50), percentage DOUBLE, studentID VARCHAR(50), grade DOUBLE);"
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
        val sql = "DELETE FROM MODULO WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                stmt.executeUpdate()
            }
        }
        return t
    }

    override fun deleteAll() {
        val sql = "DELETE FROM MODULO"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    override fun getAll(): MutableList<Grade> {
        val sql = "SELECT * FROM MODULO"
        val modulos: MutableList<Grade> = mutableListOf()
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    modulos.add(
                        Modulo(
                            moduloName = resultSet.getString("moduloName"),
                            component = ModuloComponent(resultSet.getString("moduloName")),
                            superComponentID = UUID.fromString(resultSet.getString("studentID")),
                            id = UUID.fromString(resultSet.getString("id"))
                        )
                    )
                }
            }
        }
        if (modulos.isEmpty()) throw StudentEmpty
        return modulos
    }

    override fun updateById(t: Grade): Grade {
        TODO("Not yet implemented")
    }

    override fun getById(t: Grade): Grade {
        TODO("Not yet implemented")
    }


}