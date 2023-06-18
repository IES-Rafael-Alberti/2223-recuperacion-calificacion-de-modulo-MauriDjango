package dataSource

import entities.component.Component


class CSVDSource(connection: csv.CSVHandler): DSource<csv.CSVHandler>(connection) {
    fun getStudents() = connection.getStudents()
    fun getRAComponent() = connection.getRAComponent()
    fun getCEComponents() = connection.getCEComponents()
    fun getInstrumentComponents() = connection.getInstrumentComponents()
    fun getInstrumentGrade(studentName: String, component: Component) = connection.getInstrumentGrade(studentName, component)

}

