package uket.auth.interceptor

import jakarta.servlet.http.HttpServletRequest
import uket.uket.auth.jwt.JwtValues.JWT_AUTHORIZATION_HEADER
import uket.uket.auth.jwt.JwtValues.JWT_AUTHORIZATION_VALUE_PREFIX
import uket.uket.common.BaseException
import uket.uket.common.ErrorCode

object AuthorizationExtractor {
    @JvmStatic
    fun extractAccessToken(request: HttpServletRequest): String {
        val accessToken = request.getHeader(JWT_AUTHORIZATION_HEADER)
            ?: throw BaseException(ErrorCode.AUTHENTICATION_FAILED)

        return accessToken.replace(JWT_AUTHORIZATION_VALUE_PREFIX, "")
    }
}
