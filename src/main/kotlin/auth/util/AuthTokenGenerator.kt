package uket.auth.util

import org.springframework.stereotype.Component
import uket.auth.dto.UserAuthToken
import uket.auth.jwt.JwtAuthTokenUtil
import uket.domain.user.entity.User
import uket.domain.user.enums.UserRole

@Component
class AuthTokenGenerator(
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
) {
    fun generateAuthToken(user: User): UserAuthToken {
        val userId: Long = user.id
        val name: String = user.name
        val isRegistered: Boolean = user.isRegistered

        val newAccessToken: String = jwtAuthTokenUtil.createAccessToken(userId, name, UserRole.USERS.toString(), isRegistered)
        val newRefreshToken: String = jwtAuthTokenUtil.createRefreshToken()

        return UserAuthToken.of(newAccessToken, newRefreshToken, isRegistered)
    }
}
