package uket.api.user.request

data class TokenReissueRequest(
    val accessToken: String,
    val refreshToken: String,
)
