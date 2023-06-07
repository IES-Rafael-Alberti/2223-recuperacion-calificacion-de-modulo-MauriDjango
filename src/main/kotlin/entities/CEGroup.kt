package entities

import jdk.jfr.Percentage


class CEGroup(
    val ceGroup: MutableList<String>,
    val instrGroup:  MutableList<String>,
    //TODO be care with how this below variable functions
    val cePercentage: Percentage,
    val ceGroupGrade: Float
)

data class CEGroupData(
    val ceGroup: MutableList<String>,
    val instrGroup:  MutableList<String>,
    //TODO be care with how this below variable functions
    val cePercentage: Percentage,
    val ceGroupGrade: Float
)