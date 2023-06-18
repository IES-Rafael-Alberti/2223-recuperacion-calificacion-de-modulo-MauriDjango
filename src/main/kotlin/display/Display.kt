package display

import de.m3y.kformat.Table
import de.m3y.kformat.table
import entities.grade.Grade
import entities.grade.Modulo
import entities.grade.Student


/**
 * An object that controls the interface output of the Application
 * Uses KFormat Library
 */
object Display {

    /**
     * Transforms a student and a modulo into a table
     *
     * @param student: Student object
     * @param modulo: Modulo object
     * @return CharSequence: A CharSequence that when printed to console is a table
     */
    fun toTable(student: Student, modulo: Modulo): CharSequence {
        student.let {

            return table {
                header(
                    "Name",
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

    /**
     * Transforms an RAGrade into a table
     *
     * @param raGrade: RAGrade
     * @return CharSequence: A CharSequence that when printed to console is a table
     */
    fun toTable(raGrade: Grade): CharSequence {
        raGrade.let {

            return table {
                header(
                    "RA" + raGrade.component.componentName,
                    "Percentage: " + raGrade.component.percentage.toString(),
                    "Grade: " + raGrade.getGrade().toString()
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
            }.render()
        }
    }

    /**
     * Prints a table CharSequence to console
     *
     * @param table: Table CharSequence
     */
    fun printTable(table: CharSequence) {
        println(table)
        println("\n")
    }
}