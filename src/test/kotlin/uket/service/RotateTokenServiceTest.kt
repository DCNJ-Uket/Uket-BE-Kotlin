package uket.service

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import uket.auth.jwt.JwtAuthTokenUtil
import uket.domain.user.dto.CreateUserCommand
import uket.domain.user.entity.User
import uket.domain.user.enums.Platform
import uket.domain.user.service.UserService
import uket.modules.redis.service.RotateTokenService
import uket.uket.domain.user.enums.UserRole

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RotateTokenServiceTest {
    @Autowired
    lateinit var rotateTokenService: RotateTokenService

    @Autowired
    lateinit var jwtAuthTokenUtil: JwtAuthTokenUtil

    @Autowired
    lateinit var userService: UserService

    private lateinit var testUser: User
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    @BeforeEach
    fun setUp() {
        val user = CreateUserCommand(
            platform = Platform.KAKAO,
            platformId = "12345",
            name = "김철수",
            email = "cheolsu@naver.com",
            profileImage = "https://some-url.com/profile.jpg",
        )

        testUser = userService.createUser(user)

        accessToken = jwtAuthTokenUtil.createAccessToken(
            testUser.id, testUser.name, UserRole.USERS.toString(), testUser.isRegistered, 3000L,
        )
        refreshToken = jwtAuthTokenUtil.createRefreshToken(6000L)

        rotateTokenService.storeToken(refreshToken, accessToken, testUser.id)
    }

    @Test
    fun `Redis에 token 관련 정보가 잘 저장된다`() {
        assertDoesNotThrow {
            rotateTokenService.validateRefreshToken(refreshToken)
        }
    }
}
