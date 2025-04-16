package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.auth.dto.UserAuthToken
import uket.auth.dto.response.userinfo.OAuth2UserInfoResponse
import uket.auth.filter.TokenValidator
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_VALUE_REFRESH
import uket.auth.oauth.OAuth2TokenManager
import uket.auth.util.AuthTokenGenerator
import uket.auth.util.OAuth2UserInfoManager
import uket.domain.user.dto.CreateUserCommand
import uket.domain.user.entity.User
import uket.domain.user.enums.Platform
import uket.domain.user.service.UserService
import uket.modules.redis.service.RotateTokenService

@Service
class UserAuthFacade(
    private val tokenValidator: TokenValidator,
    private val oauth2TokenManager: OAuth2TokenManager,
    private val oAuth2UserInfoManager: OAuth2UserInfoManager,
    private val userService: UserService,
    private val authTokenGenerator: AuthTokenGenerator,
    private val rotateTokenService: RotateTokenService,
) {
    @Transactional
    fun login(platform: Platform, redirectUri: String, code: String): UserAuthToken {
        val tokenResponse = oauth2TokenManager.getAccessToken(platform, redirectUri, code)
        val userInfo = oAuth2UserInfoManager.getUserInfo(platform, tokenResponse)

        val newUser = userService.createUser(generateCreateUserDto(userInfo))

        val userAuthToken: UserAuthToken = authTokenGenerator.generateAuthToken(newUser)

        rotateTokenService.storeToken(userAuthToken.refreshToken, userAuthToken.accessToken, newUser.id)
        return userAuthToken
    }

    fun reissue(accessToken: String, refreshToken: String): UserAuthToken {
        tokenValidator.checkNotExpiredToken(accessToken)
        val existingAccessToken: String? = rotateTokenService.getAccessTokenForToken(refreshToken)
        if (accessToken != existingAccessToken) {
            throw IllegalArgumentException("요청하신 AccessToken이 저장소에 존재하지 않습니다. 확인 부탁드립니다.")
        }

        rotateTokenService.validateRefreshToken(refreshToken)

        tokenValidator.validateExpiredToken(refreshToken)
        tokenValidator.validateTokenCategory(JWT_PAYLOAD_VALUE_REFRESH, refreshToken)
        tokenValidator.validateTokenSignature(refreshToken)

        val findUser: User = userService.getById(rotateTokenService.getUserIdForToken(refreshToken))

        val userAuthToken: UserAuthToken = authTokenGenerator.generateAuthToken(findUser)

        rotateTokenService.storeToken(userAuthToken.refreshToken, userAuthToken.accessToken, findUser.id)
        return userAuthToken
    }

    private fun generateCreateUserDto(userInfo: OAuth2UserInfoResponse): CreateUserCommand = CreateUserCommand(
        platform = Platform.fromString(userInfo.provider),
        platformId = userInfo.providerId,
        email = userInfo.email,
        name = userInfo.name,
        profileImage = userInfo.profileImage,
    )
}
