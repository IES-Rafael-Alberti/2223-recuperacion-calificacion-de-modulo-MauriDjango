package dao

import entities.grade.Grade
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

class StudentDAO(private val dataSource: DataSource): DAO<Student> {
    private val logger = LoggerFactory.getLogger("StudentDAO")

    override fun create(t: Student): Student {
        try {
            val sql = "INSERT INTO STUDENT (id, studentname) VALUES (?, ?)"
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, t.id.toString())
                    stmt.setString(2, t.name)
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
                "CREATE TABLE STUDENT (id VARCHAR(50) PRIMARY KEY , studentName VARCHAR(100));"
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
                            id = UUID.fromString(resultSet.getString("id"))
                        )
                    )
                }
            }
        }
        if (students.isEmpty()) throw StudentEmpty
        return students
    }

    override fun updateById(t: Student): Student {
        TODO("Not yet implemented")
    }

    override fun getById(t: Student): Student {
        TODO("Not yet implemented")
    }

}

