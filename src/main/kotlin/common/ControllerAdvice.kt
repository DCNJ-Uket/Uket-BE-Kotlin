package uket.common

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import uket.common.response.PublicErrorResponse
import java.io.IOException
import java.nio.CharBuffer
import java.security.InvalidParameterException
import java.time.format.DateTimeParseException
import io.swagger.v3.oas.annotations.Hidden

@Hidden
@RestControllerAdvice
class ControllerAdvice {
    private val log by LoggerDelegate()

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun unknownException(
        request: HttpServletRequest,
        exception: Exception,
    ): PublicErrorResponse {
        log.error("[UnhandledException] ${dumpRequest(request)}", exception)

        return PublicErrorResponse.error(exception = exception)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(
        request: HttpServletRequest,
        exception: DateTimeParseException,
    ): PublicErrorResponse {
        return PublicErrorResponse.warn(exception = exception)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        request: HttpServletRequest,
        exception: IllegalStateException,
    ): PublicErrorResponse {
        return PublicErrorResponse.error(exception = exception)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        request: HttpServletRequest,
        exception: IllegalArgumentException,
    ): PublicErrorResponse {
        return PublicErrorResponse.error(exception = exception)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HttpMessageNotReadableException::class,
        InvalidParameterException::class,
        ServletRequestBindingException::class,
        MethodArgumentNotValidException::class,
        ConstraintViolationException::class,
    )
    fun handleValidationException(
        request: HttpServletRequest,
        exception: Exception,
    ): PublicErrorResponse {
        return PublicErrorResponse.warn(exception = exception)
    }

    @ExceptionHandler(PublicException::class)
    fun handlePublicException(
        request: HttpServletRequest,
        exception: PublicException,
    ): ResponseEntity<PublicErrorResponse> {
        return ResponseEntity.status(exception.httpStatus).body(
            PublicErrorResponse.withErrorLevel(
                title = exception.title,
                publicMessage = exception.publicMessage,
                systemMessage = exception.systemMessage,
                errorLevel = exception.errorLevel,
                exception = exception
            )
        )
    }

    private fun dumpRequest(request: HttpServletRequest): StringBuilder {
        val dump = StringBuilder("HttpRequest Dump:").append("\n  Remote Addr   : ").append(request.remoteAddr)
            .append("\n  Protocol      : ").append(request.protocol).append("\n  Request Method: ")
            .append(request.method).append("\n  Request URI   : ").append(request.requestURI)
            .append("\n  Query String  : ").append(request.queryString).append("\n  Parameters    : ")

        val parameterNames = request.parameterNames
        while (parameterNames.hasMoreElements()) {
            val name = parameterNames.nextElement()
            dump.append("\n    ").append(name).append('=')
            val values = request.getParameterValues(name)
            values?.forEach { dump.append(it) }
        }

        dump.append("\n  Headers       : ")
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val name = headerNames.nextElement()
            dump.append("\n    ").append(name).append(":")
            val headerValues = request.getHeaders(name)
            while (headerValues.hasMoreElements()) {
                dump.append(headerValues.nextElement())
            }
        }

        dump.append("\n  Body       : ")
        if (request.contentType?.contains("application/x-www-form-urlencoded") == true) {
            dump.append("\n    Body is not readable for 'application/x-www-form-urlencoded' type or has been read")
        } else {
            try {
                dump.append("\n    ").append(readableToString(request.reader))
            } catch (ex: IOException) {
                dump.append("\n    NOT_READABLE")
            } catch (ex: IllegalStateException) {
                dump.append("\n    BODY_ALREADY_READ")
            }
        }

        return dump
    }

    private fun readableToString(readable: Readable): String {
        val stringBuilder = StringBuilder()
        val buffer = CharBuffer.allocate(1024)

        while (readable.read(buffer) != -1) {
            buffer.flip()
            stringBuilder.append(buffer)
            buffer.clear()
        }

        return stringBuilder.toString()
    }
}
