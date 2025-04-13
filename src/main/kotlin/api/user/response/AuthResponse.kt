package uket.api.user.response

import uket.auth.dto.AuthToken
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
        fun of(user: User, authToken: AuthToken): AuthResponse = AuthResponse(
            id = user.id,
            name = user.name,
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            isRegistered = authToken.isRegistered
        )
    }
}
