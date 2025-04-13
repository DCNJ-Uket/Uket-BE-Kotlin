package uket.common

import org.springframework.http.HttpStatus

open class PublicException(
    val publicMessage: String? = null,
    val systemMessage: String? = publicMessage,
    val title: String? = null,
    val errorLevel: ErrorLevel = ErrorLevel.ERROR,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
) : RuntimeException()

class PublicNotFoundException(
    publicMessage: String? = null,
    systemMessage: String? = publicMessage,
    title: String? = null,
    errorLevel: ErrorLevel = ErrorLevel.ERROR,
) : PublicException(publicMessage, systemMessage, title, errorLevel, HttpStatus.NOT_FOUND)

enum class ErrorLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}
