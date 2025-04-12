package uket.auth.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import uket.auth.filter.TokenValidator
import uket.auth.interceptor.AuthorizationExtractor.extractAccessToken
import uket.common.BaseException
import uket.common.ErrorCode
import uket.common.response.ErrorResponse
import java.io.IOException
import java.nio.charset.StandardCharsets

@Component
class LoginInterceptor(
    private val tokenValidator: TokenValidator,
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (isPreflight(request) || isSwaggerRequest(request)) {
            return true
        }

        val token = extractAccessToken(request)
        try {
            tokenValidator.validateRegistered(token)
        } catch (exception: BaseException) {
            val errorCode: ErrorCode = exception.errorCode
            writeErrorResponse(response, errorCode)
            return false
        }
        return true
    }

    @Throws(IOException::class)
    private fun writeErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer
            .write(objectMapper.writeValueAsString(ErrorResponse.from(errorCode)))
    }

    private fun isSwaggerRequest(request: HttpServletRequest): Boolean {
        val uri = request.requestURI
        return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars")
    }

    private fun isPreflight(request: HttpServletRequest): Boolean = request.method == HttpMethod.OPTIONS.toString()
}
