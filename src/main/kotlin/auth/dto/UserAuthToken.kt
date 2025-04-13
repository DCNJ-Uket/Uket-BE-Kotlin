package uket.auth.dto

data class UserAuthToken(
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean,
) {
    companion object {
        fun of(accessToken: String, refreshToken: String, isRegistered: Boolean): UserAuthToken =
            UserAuthToken(accessToken, refreshToken, isRegistered)
    }
}
