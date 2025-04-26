package uket.api.user.response

import uket.auth.dto.UserAuthToken

data class UserTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean,
) {
    companion object {
        fun from(authToken: UserAuthToken): UserTokenResponse = UserTokenResponse(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            isRegistered = authToken.isRegistered
        )
    }
}
