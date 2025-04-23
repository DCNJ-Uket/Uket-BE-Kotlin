package uket.api.user.response

import uket.auth.dto.UserAuthToken
import uket.domain.user.entity.User

@JvmRecord
data class AuthResponse(
    val id: Long,
    val name: String,
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean,
) {
    companion object {
        fun of(user: User, userAuthToken: UserAuthToken): AuthResponse = AuthResponse(
            id = user.id,
            name = user.name,
            accessToken = userAuthToken.accessToken,
            refreshToken = userAuthToken.refreshToken,
            isRegistered = userAuthToken.isRegistered
        )
    }
}
