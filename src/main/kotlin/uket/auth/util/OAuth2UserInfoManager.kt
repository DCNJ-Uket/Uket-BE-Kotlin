package uket.auth.util

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import uket.auth.dto.response.token.OAuth2TokenResponse
import uket.auth.dto.response.userinfo.GoogleUserInfoResponse
import uket.auth.dto.response.userinfo.KakaoUserInfoResponse
import uket.auth.dto.response.userinfo.OAuth2UserInfoResponse
import uket.auth.oauth.OAuth2Manager
import uket.auth.properties.AppProperties
import uket.domain.user.enums.Platform
import java.net.URI

@Component
class OAuth2UserInfoManager(
    private val appProperties: AppProperties,
) : OAuth2Manager() {
    fun getUserInfo(
        platform: Platform,
        tokenResponse: OAuth2TokenResponse,
    ): OAuth2UserInfoResponse {
        if (platform === Platform.KAKAO) {
            return getKakaoUserInfoResponse(tokenResponse)
        }

        if (platform === Platform.GOOGLE) {
            return getGoogleUserInfoResponse(tokenResponse)
        }

        throw IllegalArgumentException("유효하지 않은 플랫폼입니다.")
    }

    private fun getKakaoUserInfoResponse(tokenResponse: OAuth2TokenResponse): OAuth2UserInfoResponse {
        val restClient: RestClient = createRestClient(appProperties.kakao.userInfoUri)
        val authorization = java.lang.String.join(" ", tokenResponse.tokenType, tokenResponse.accessToken)

        val response = requestUserInfoToKakao(restClient, authorization)

        if (response != null) {
            return KakaoUserInfoResponse(response)
        }
        throw IllegalArgumentException("OAuth2 요청이 실패했습니다.")
    }

    private fun requestUserInfoToKakao(restClient: RestClient, authorization: String): Map<String, Any>? = restClient
        .post()
        .uri { uriBuilder: UriBuilder -> this.getKakaoUserInfoUri(uriBuilder) }
        .header(HttpHeaders.AUTHORIZATION, authorization)
        .header(HttpHeaders.CONTENT_TYPE, MEDIA_TYPE)
        .retrieve()
        .body(object : ParameterizedTypeReference<Map<String, Any>>() {
        })

    private fun getKakaoUserInfoUri(uriBuilder: UriBuilder): URI = uriBuilder
        .queryParam("grant_type", "authorization_code")
        .queryParam("client_id", appProperties.kakao.clientId)
        .queryParam("client_secret", appProperties.kakao.clientSecret)
        .queryParam("property_keys", "[\"kakao_account.email\", \"kakao_account.name\", \"kakao_account.profile\"]")
        .build()

    private fun getGoogleUserInfoResponse(tokenResponse: OAuth2TokenResponse): OAuth2UserInfoResponse {
        val restClient: RestClient = createRestClient(appProperties.google.userInfoUri)
        val authorization = java.lang.String.join(" ", tokenResponse.tokenType, tokenResponse.accessToken)

        val response = requestUserInfoToGoogle(restClient, authorization)

        if (response != null) {
            return GoogleUserInfoResponse(response)
        }
        throw IllegalArgumentException("OAuth2 요청이 실패했습니다.")
    }

    private fun requestUserInfoToGoogle(restClient: RestClient, authorization: String): Map<String, Any>? = restClient
        .get()
        .uri(googleUserInfoUri)
        .header(HttpHeaders.AUTHORIZATION, authorization)
        .retrieve()
        .body(object : ParameterizedTypeReference<Map<String, Any>>() {})

    private val googleUserInfoUri: URI
        get() {
            val baseUrl: String = appProperties.google.userInfoUri
            val uriBuilder = UriComponentsBuilder.fromUriString(baseUrl)
            return uriBuilder.build().toUri()
        }
}
