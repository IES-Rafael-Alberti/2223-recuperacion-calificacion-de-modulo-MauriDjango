package dataSource

import csv.CSVHandler
import entities.component.Component


abstract class DSource<T>(val connection: T) {
}

