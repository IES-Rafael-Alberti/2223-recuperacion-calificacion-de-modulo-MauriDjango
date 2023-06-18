import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource


/**
 * An object that generates a Datasource
 */
object DataSourceFactory {
    enum class DataSourceType {
        HIKARI,
        JDBC
    }

    /**
     * Generates a Datasource based on DataSourceType
     *
     * @param dataSourceType: DataSourceType
     * @return Datasource
     */
    fun getDS(dataSourceType: DataSourceType): DataSource {
        return when (dataSourceType) {
            DataSourceType.HIKARI -> {
                val config = HikariConfig()
                config.jdbcUrl = "jdbc:h2:./default"
                config.username = "user"
                config.password = "user"
                config.driverClassName = "org.h2.Driver"
                config.maximumPoolSize = 10
                config.isAutoCommit = true
                config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                HikariDataSource(config)
            }

            DataSourceType.JDBC -> TODO()
        }
    }
}

val hikarih2ds = DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI)