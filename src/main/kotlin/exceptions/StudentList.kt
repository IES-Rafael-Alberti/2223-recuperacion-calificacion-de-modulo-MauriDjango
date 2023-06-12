package exceptions

object StudentList : Throwable() {
    val msg = "Theres was an error extracting the students from the file"
}

/*
if (studentList.isNullOrEmpty()) {
    logger.warnLog(StudentListError.msg)
    throw StudentListError
}*/
