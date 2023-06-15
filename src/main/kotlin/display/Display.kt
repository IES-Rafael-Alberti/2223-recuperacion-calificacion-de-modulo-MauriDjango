package display

import de.m3y.kformat.Table
import de.m3y.kformat.table
import entities.grade.Grade
import entities.grade.Modulo
import entities.grade.RAGrade
import entities.grade.Student

object Display {
    fun toTable(student: Student, modulo: Modulo): CharSequence {
        student.let {

            return table {
                header(
                    student.initials,
                    student.name
                )
                row(
                    modulo.moduloName,
                    modulo.getGrade()
                )

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)
                    alignment(1, Table.Hints.Alignment.RIGHT)
                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render().trim()
        }
    }

    fun toTable(raGrade: Grade): CharSequence {
        raGrade.let {

            return table {
                header(
                    "RA" + raGrade.component.componentName,
                    "Percentage " + raGrade.component.percentage.toString(),
                    "Grade " + raGrade.getGrade().toString()
                )
                raGrade.subComponents.forEach { ce ->
                    row(
                        "CE." + ce.component.componentName,
                        ce.component.percentage.toString(),
                        ce.getGrade().toString()
                    )
                }

                hints {
                    alignment(0, Table.Hints.Alignment.LEFT)
                    alignment(1, Table.Hints.Alignment.RIGHT)
                    borderStyle = Table.BorderStyle.SINGLE_LINE
                }
            }.render().trim()
        }
    }

    fun printTable(table: CharSequence) {
        println(table)
        println("\n")
    }
}