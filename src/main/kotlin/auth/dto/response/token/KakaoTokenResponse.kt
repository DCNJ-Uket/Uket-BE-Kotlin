package uket.auth.dto.response.token

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokenResponse(
    @JsonProperty("token_type")
    override val tokenType: String,

    @JsonProperty("access_token")
    override val accessToken: String,

    @JsonProperty("expires_in")
    private val expiredIn: Long,

    @JsonProperty("refresh_token")
    private val refreshToken: String,

    @JsonProperty("refresh_token_expires_in")
    private val refreshTokenExpiresIn: Long,

    @JsonProperty("scope")
    override val scope: String,
) : OAuth2TokenResponse
