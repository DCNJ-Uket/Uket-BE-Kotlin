package uket.api.admin.handler

import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uket.common.LoggerDelegate
import uket.common.response.ErrorResponse
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.CharBuffer
import java.security.InvalidParameterException
import java.time.format.DateTimeParseException

@Hidden
@RestControllerAdvice
class WebExceptionHandler() {
    private val log by LoggerDelegate()

    @ExceptionHandler(Exception::class)
    fun unKnownException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ErrorResponse> {
        return handleUnhandledException(request, exception)
    }

    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(
        request: HttpServletRequest,
        exception: DateTimeParseException,
    ): ResponseEntity<ErrorResponse> {
        log.warn("[DateTimeParseException] {}", exception.message, exception)
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.from("날짜 형식이 올바르지 않습니다."))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        request: HttpServletRequest,
        exception: IllegalStateException,
    ): ResponseEntity<ErrorResponse> {
        log.warn("[IllegalStateException] {}", exception.message, exception)
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.from(exception.message ?: "잘못된 요청입니다."))
    }

    @ExceptionHandler(Throwable::class)
    fun handleUnhandledException(
        request: HttpServletRequest,
        exception: Throwable,
    ): ResponseEntity<ErrorResponse> {
        val dump = dumpRequest(request).append("\n ")
            .append(getStackTraceAsString(exception))

        log.error("[UnhandledException] {}\n", dump)

        return ResponseEntity
            .internalServerError()
            .body(ErrorResponse.from("일시적으로 접속이 원활하지 않습니다. 서버 팀에 문의 부탁드립니다."))
    }

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
    ): ResponseEntity<ErrorResponse> {
        log.warn("[ValidationException]", exception)
        return ResponseEntity
            .badRequest()
            .body(ErrorResponse.from("요청 값이 올바르지 않습니다."))
    }

    private fun getStackTraceAsString(throwable: Throwable): String {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    private fun dumpRequest(request: HttpServletRequest): StringBuilder {
        val dump = StringBuilder("HttpRequest Dump:")
            .append("\n  Remote Addr   : ").append(request.remoteAddr)
            .append("\n  Protocol      : ").append(request.protocol)
            .append("\n  Request Method: ").append(request.method)
            .append("\n  Request URI   : ").append(request.requestURI)
            .append("\n  Query String  : ").append(request.queryString)
            .append("\n  Parameters    : ")

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
