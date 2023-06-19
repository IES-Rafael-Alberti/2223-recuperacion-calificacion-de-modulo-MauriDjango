package dataSource


/**
 * Abstract class representing a data source.
 *
 * @param connection The connection object used for data retrieval.
 * @param T The type of the connection object.
 */
abstract class DSource<T>(val connection: T)
