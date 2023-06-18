package dao


/**
 * Data Access Object
 * An interface that is in charge of interacting with the database
 */
interface DAO<T> {
    /**
     * Inserts an object of type T into a Database
     *
     * @param t: Generic Type
     * @return t: Generic Type
     */
    fun  create(t: T):T

    /**
     * Creates a new Table in Database
     */
    fun createTable()

    /**
     * Finds an entity in the database and returns an object
     *
     * @param t: Generic Type
     * @return t: Generic Type
     */
    fun getById(t: T):T

    /**
     * Updates an entity in the database with values from an object
     *
     * @param t: Generic Type
     * @return t: Generic Type
     */
    fun updateById(t: T):T

    /**
     * Deletes an entity from the database that matches with an object
     * @param t: Generic Type
     * @return t: Generic Type
     */
    fun deleteById(t: T):T

    /**
     * Deletes all entities from a table in the database
     */
    fun deleteAll()

    /**
     * Retrieves all entities from a table in the database
     *
     * @return MutableList<T>: A list containing all entities found transformed to objects
     */
    fun getAll(): MutableList<T>
}