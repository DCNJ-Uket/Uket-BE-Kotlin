package uket.gateway.interceptor

import jakarta.servlet.http.HttpServletRequest
import uket.gateway.exception.BaseException
import uket.gateway.exception.ErrorCode
import uket.gateway.jwt.JwtValues.JWT_AUTHORIZATION_HEADER
import uket.gateway.jwt.JwtValues.JWT_AUTHORIZATION_VALUE_PREFIX

object AuthorizationExtractor {
    @JvmStatic
    fun extractAccessToken(request: HttpServletRequest): String {
        val accessToken = request.getHeader(JWT_AUTHORIZATION_HEADER)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED)

        return accessToken.replace(JWT_AUTHORIZATION_VALUE_PREFIX, "")
    }
}
