package uket.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import uket.common.ErrorCode
import uket.common.response.ErrorResponse
import java.nio.charset.StandardCharsets

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        writeErrorResponse(response)
    }

    private fun writeErrorResponse(
        response: HttpServletResponse,
    ) {
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer
            .write(objectMapper.writeValueAsString(ErrorResponse.from(ErrorCode.TOKEN_AUTHENTICATION_FAILED)))
    }
}
