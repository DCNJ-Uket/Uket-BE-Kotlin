package uket.domain.user.service

import org.springframework.stereotype.Component
import uket.auth.dto.AuthToken
import uket.auth.jwt.JwtAuthTokenUtil
import uket.domain.user.entity.User
import uket.domain.user.enums.UserRole

@Component
class AuthTokenGenerator(
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
) {
    fun generateAuthToken(user: User): AuthToken {
        val userId: Long = user.id
        val name: String = user.name
        val isRegistered: Boolean = user.isRegistered

        val newAccessToken: String = jwtAuthTokenUtil.createAccessToken(userId, name, UserRole.USERS.toString(), isRegistered)
        val newRefreshToken: String = jwtAuthTokenUtil.createRefreshToken()

        return AuthToken.of(newAccessToken, newRefreshToken, isRegistered)
    }
}
