package uket.auth.oauth

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder
import uket.auth.dto.response.token.GoogleTokenResponse
import uket.auth.dto.response.token.KakaoTokenResponse
import uket.auth.dto.response.token.OAuth2TokenResponse
import uket.auth.properties.AppProperties
import uket.domain.user.enums.Platform
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Component
class OAuth2TokenManager(
    private val appProperties: AppProperties,
) : OAuth2Manager() {
    fun getAccessToken(platform: Platform, redirectUri: String, code: String): OAuth2TokenResponse {
        if (platform === Platform.KAKAO) {
            return getKakaoTokenResponse(redirectUri, code)
        }

        if (platform === Platform.GOOGLE) {
            return getGoogleTokenResponse(redirectUri, code)
        }

        throw IllegalArgumentException("유효하지 않은 플랫폼입니다.")
    }

    private fun getKakaoTokenResponse(redirectUri: String, code: String): OAuth2TokenResponse {
        val restClient: RestClient = createRestClient(appProperties.kakao.tokenUri)

        val response: OAuth2TokenResponse? = restClient
            .post()
            .uri { uriBuilder: UriBuilder -> getKakaoTokenUri(redirectUri, code, uriBuilder) }
            .header(HttpHeaders.CONTENT_TYPE, MEDIA_TYPE)
            .retrieve()
            .body(KakaoTokenResponse::class.java)

        if (response != null) {
            return response
        }
        throw IllegalArgumentException("OAuth2 요청이 실패했습니다.")
    }

    private fun getKakaoTokenUri(redirectUri: String, code: String, uriBuilder: UriBuilder): URI = uriBuilder
        .queryParam("grant_type", "authorization_code")
        .queryParam("redirect_uri", redirectUri)
        .queryParam("client_id", appProperties.kakao.clientId)
        .queryParam("client_secret", appProperties.kakao.clientSecret)
        .queryParam("code", code)
        .build()

    private fun getGoogleTokenResponse(redirectUri: String, code: String): OAuth2TokenResponse {
        val restClient: RestClient = createRestClient(appProperties.google.tokenUri)

        val decode = URLDecoder.decode(code, StandardCharsets.UTF_8)

        val response: OAuth2TokenResponse? = restClient
            .post()
            .uri { uriBuilder: UriBuilder -> getGoogleTokenUri(redirectUri, decode, uriBuilder) }
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .body(GoogleTokenResponse::class.java)

        if (response != null) {
            return response
        }
        throw IllegalStateException("OAuth2 요청이 실패했습니다.")
    }

    private fun getGoogleTokenUri(redirectUri: String, code: String, uriBuilder: UriBuilder): URI = uriBuilder
        .queryParam("grant_type", "authorization_code")
        .queryParam("redirect_uri", redirectUri)
        .queryParam("client_id", appProperties.google.clientId)
        .queryParam("client_secret", appProperties.google.clientSecret)
        .queryParam("code", code)
        .build()
}
