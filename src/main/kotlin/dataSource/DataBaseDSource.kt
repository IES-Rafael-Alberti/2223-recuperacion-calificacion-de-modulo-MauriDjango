package dataSource

import javax.sql.DataSource

class DataBaseDSource(connection: DataSource): DSource<DataSource>(connection) {
}

