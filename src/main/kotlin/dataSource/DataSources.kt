package dataSource
enum class DataSources {
    HikariH2, CVS
}

abstract class Source<T>() {
    abstract val dataSource: T
}

