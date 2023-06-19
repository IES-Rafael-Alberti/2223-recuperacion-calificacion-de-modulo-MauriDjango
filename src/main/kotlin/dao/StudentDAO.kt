package dao

import entities.component.RAComponent
import entities.grade.Grade
import entities.grade.RAGrade
import entities.grade.Student
import exceptions.StudentEmpty
import hikarih2ds
import org.h2.message.DbException
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

var studentDAO = StudentDAO(hikarih2ds)

/**
 * A class that implements DAO
 * Accesses the Instrument aspects of the DB
 *
 * @param dataSource: A datasource that provides the connection to the database
 * @property logger: Logger
 */
class StudentDAO(private val dataSource: DataSource): DAO<Student> {
    private val logger = LoggerFactory.getLogger("StudentDAO")

    override fun create(t: Student): Student {
        try {
            val sql = "INSERT INTO STUDENT (id, studentname, initials) VALUES (?, ?, ?)"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.name)
                    stmt.setString(3, t.initials)
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
                "CREATE TABLE STUDENT (id VARCHAR(50) PRIMARY KEY , studentName VARCHAR(100), initials VARCHAR(4));"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val result = stmt.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            logger.warn(e.message)
        }
    }

    override fun deleteById(t: Student): Student {
        val sql = "DELETE FROM STUDENT WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                stmt.executeUpdate()
            }
        }
        return t
    }

    override fun deleteAll() {
        val sql = "DELETE FROM STUDENT"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    override fun getAll(): MutableList<Student> {
        val sql = "SELECT * FROM STUDENT;"
        val students: MutableList<Student> = mutableListOf()

        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val resultSet = stmt.executeQuery()
                while (resultSet.next()) {
                    students.add(
                        Student(
                            name = resultSet.getString("studentName"),
                            initials = resultSet.getString("Initials"),
                            id = UUID.fromString(resultSet.getString("id"))
                        )
                    )
                }
            }
        }
        return students
    }

    override fun updateById(t: Student): Int {
        val sql = "UPDATE STUDENT SET  studentName = ?, initials = ? WHERE id = ?"
        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.name)
                stmt.setString(2, t.initials)
                stmt.setString(3, t.id.toString())
                stmt.executeUpdate()
            }
        }
    }

    override fun getById(t: Student): Student? {
        val sql = "SELECT * FROM STUDENT WHERE id = ?"
        dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, t.id.toString())
                val resultSet = stmt.executeQuery()
                if (resultSet.next()) {
                    return Student(
                        name = resultSet.getString("studentName"),
                        initials = resultSet.getString("Initials"),
                        id = UUID.fromString(resultSet.getString("id"))
                    )
                } else {
                    return null
                }
            }
        }
    }
}

