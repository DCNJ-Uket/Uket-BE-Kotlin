package uket.auth.filter

data class AuthenticationUserInfo(
    val userId: Long,
    val name: String,
    val role: String,
    val isRegistered: Boolean = false,
)
