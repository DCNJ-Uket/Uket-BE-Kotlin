package uket.api.user.request

data class LoginRequest(
    val code: String,
    val redirectUri: String,
)
