package uket.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import uket.common.ErrorCode
import uket.common.response.ErrorResponse
import java.nio.charset.StandardCharsets

@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    private val log = LoggerFactory.getLogger(JwtAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        writeErrorResponse(response)
        log.error("[JwtAccessDeniedHandler] Access Denied Exception", accessDeniedException)
    }

    private fun writeErrorResponse(
        response: HttpServletResponse,
    ) {
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.from(ErrorCode.AUTHORIZATION_FAILED)))
    }
}
