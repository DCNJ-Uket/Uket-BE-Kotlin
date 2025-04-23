package uket.auth.dto.response.token

interface OAuth2TokenResponse {
    val tokenType: String
    val accessToken: String
    val scope: String
}
