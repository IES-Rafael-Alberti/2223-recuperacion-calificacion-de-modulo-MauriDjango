package dao

interface DAO<T> {
    abstract fun  create():T
    abstract fun getById():T
    abstract fun updateById():T
    abstract fun deleteById():T
}