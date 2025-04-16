package uket.auth.filter

import org.springframework.stereotype.Component
import uket.auth.jwt.JwtAuthTokenUtil

@Component
class TokenValidator(
    val jwtAuthTokenUtil: JwtAuthTokenUtil,
) {
    fun validateTokenSignature(token: String?) {
        if (!jwtAuthTokenUtil.isValidToken(token)) {
            throw IllegalStateException("유효하지 않은 토큰입니다.")
        }
    }

    fun validateExpiredToken(token: String?) {
        if (jwtAuthTokenUtil.isExpired(token)) {
            throw IllegalStateException("만료된 토큰입니다.")
        }
    }

    fun validateTokenCategory(category: String, token: String?) {
        val tokenCategory: String = jwtAuthTokenUtil.getCategory(token)
        if (tokenCategory != category) {
            throw IllegalStateException("올바르지 않은 유형의 토큰입니다.")
        }
    }

    fun checkNotExpiredToken(token: String?) {
        if (!jwtAuthTokenUtil.isExpired(token)) {
            throw IllegalStateException("아직 토큰이 만료되지 않았습니다.")
        }
    }

    fun validateRegistered(accessToken: String?) {
        if (!jwtAuthTokenUtil.isRegistered(accessToken)) {
            throw IllegalStateException("가입되지 않은 사용자입니다. 회원가입 후 이용해주세요")
        }
    }
}
