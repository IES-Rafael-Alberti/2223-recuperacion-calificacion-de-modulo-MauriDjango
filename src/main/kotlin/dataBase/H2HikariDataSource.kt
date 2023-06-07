import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class H2HikariDataSource() {

    private var logger = LoggerFactory.getLogger("HikariDataSource")
    var dataSource: DataSource

    init {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:h2:./default"
        config.username = "user"
        config.password = "user"
        config.driverClassName = "org.h2.Driver"
        config.maximumPoolSize = 10
        config.isAutoCommit = true
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        dataSource = HikariDataSource(config)
        logger.debug("H2HikariDataSource initialized")
    }
}

val h2DS = H2HikariDataSource().dataSource

