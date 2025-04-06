package uket.auth.interceptor

import jakarta.servlet.http.HttpServletRequest
import uket.auth.jwt.JwtValues.JWT_AUTHORIZATION_HEADER
import uket.auth.jwt.JwtValues.JWT_AUTHORIZATION_VALUE_PREFIX
import uket.common.BaseException
import uket.common.ErrorCode

object AuthorizationExtractor {
    @JvmStatic
    fun extractAccessToken(request: HttpServletRequest): String {
        val accessToken = request.getHeader(JWT_AUTHORIZATION_HEADER)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED)

        return accessToken.replace(JWT_AUTHORIZATION_VALUE_PREFIX, "")
    }
}
