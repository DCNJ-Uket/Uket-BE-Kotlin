package uket.auth.dto

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean,
) {
    companion object {
        fun of(accessToken: String, refreshToken: String, isRegistered: Boolean): AuthToken = AuthToken(accessToken, refreshToken, isRegistered)
    }
}
