package bingo.inputoutput.exceptions.log

import org.slf4j.LoggerFactory

//TODO Check usages and refactor to instantiate logger class using logger factory in class
class Logging(className: String) {

    private val logger = LoggerFactory.getLogger(className)

    fun infoLog(s: String) = logger.info(s)
    fun debugLog(s: String) = logger.debug(s)
    fun warnLog(s: String?) = logger.warn(s)
    fun errorLog(s: String) = logger.error(s)
    fun traceLog(s: String) = logger.trace(s)
}
