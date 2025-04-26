package uket.auth.dto.response.token

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse(
    @JsonProperty("token_type")
    override val tokenType: String,

    @JsonProperty("access_token")
    override val accessToken: String,

    @JsonProperty("scope")
    override var scope: String,

    @JsonProperty("expires_in")
    private val expiredIn: Long,

    @JsonProperty("id_token")
    private val refreshToken: String,
) : OAuth2TokenResponse
