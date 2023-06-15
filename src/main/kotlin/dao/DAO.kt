package dao

import entities.grade.Student
import java.sql.ResultSet

interface DAO<T> {
    fun  create(t: T):T
    fun createTable()
    fun getById(t: T):T
    fun updateById(t: T):T
    fun deleteById(t: T):T
    fun deleteAll()
    fun getAll(): MutableList<T>
}