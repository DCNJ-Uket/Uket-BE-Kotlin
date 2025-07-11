package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.LoginRequest
import uket.api.user.request.TokenReissueRequest
import uket.api.user.request.UpdateUserInfoRequest
import uket.api.user.request.UserRegisterRequest
import uket.api.user.response.AuthResponse
import uket.api.user.response.UserDeleteResponse
import uket.api.user.response.UserInfoResponse
import uket.api.user.response.UserTicketResponse
import uket.api.user.response.UserTokenResponse
import uket.auth.config.userId.LoginUserId
import uket.auth.dto.UserAuthToken
import uket.auth.jwt.JwtAuthTokenUtil
import uket.common.response.ListResponse
import uket.domain.user.dto.RegisterUserCommand
import uket.domain.user.enums.Platform
import uket.domain.user.service.UserService
import uket.facade.FindUserTicketsFacade
import uket.facade.UserAuthFacade
import java.time.LocalDateTime

@Tag(name = "멤버 관련 API", description = "멤버 관련 API 입니다")
@RestController
class UserController(
    private val userAuthFacade: UserAuthFacade,
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
    private val userService: UserService,
    private val findUserTicketsFacade: FindUserTicketsFacade,
) {
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행합니다.")
    @PostMapping("auth/login/{provider}")
    fun login(
        @RequestBody request: LoginRequest,
        @PathVariable provider: String,
    ): ResponseEntity<AuthResponse> {
        val platform = Platform.fromString(provider)
        val authToken = userAuthFacade.login(platform, request.redirectUri, request.code)

        val userId = jwtAuthTokenUtil.getId(authToken.accessToken)
        val loginUser = userService.getById(userId)

        val response = AuthResponse.of(loginUser, authToken)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "토큰 갱신", description = "토큰을 갱신합니다")
    @PostMapping("auth/reissue")
    fun reissue(
        @RequestBody request: TokenReissueRequest,
    ): ResponseEntity<UserTokenResponse> {
        val authToken: UserAuthToken = userAuthFacade.reissue(request.accessToken, request.refreshToken)
        val response: UserTokenResponse = UserTokenResponse.from(authToken)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "유저 회원가입", description = "유저의 추가 정보를 등록하고 회원가입을 완료합니다")
    @PostMapping("/users/register")
    fun register(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
        @RequestBody
        request: UserRegisterRequest,
    ): ResponseEntity<AuthResponse> {
        val registerUserCommand = RegisterUserCommand(
            userId = userId,
            depositorName = request.depositorName,
            phoneNumber = request.phoneNumber
        )
        val authToken = userAuthFacade.register(registerUserCommand)

        val user = userService.getById(userId)
        val response = AuthResponse.of(user, authToken)

        return ResponseEntity.ok(response)
    }

    @Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다")
    @GetMapping("/users/info")
    fun getUserInfo(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
    ): ResponseEntity<UserInfoResponse> {
        val user = userService.getById(userId)
        return ResponseEntity.ok(UserInfoResponse.from(user))
    }

    @Operation(summary = "유저 정보 수정", description = "유저 정보를 수정합니다")
    @PatchMapping("/users/info")
    fun updateUserInfo(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
        @RequestBody
        request: UpdateUserInfoRequest,
    ): ResponseEntity<UserInfoResponse> {
        val user = userService.updateUserInfo(
            userId = userId,
            email = request.email,
            name = request.name,
            profileImage = request.profileImage,
            depositorName = request.depositorName,
            phoneNumber = request.phoneNumber
        )
        return ResponseEntity.ok(UserInfoResponse.from(user))
    }

    @Operation(summary = "유저 티켓 목록 조회", description = "유저의 보유 티켓 목록을 조회합니다")
    @GetMapping("/users/tickets")
    fun getUserTickets(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
    ): ResponseEntity<ListResponse<UserTicketResponse>> {
        val responses = findUserTicketsFacade.findUserTickets(userId, LocalDateTime.now())
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "유저 회원 탈퇴", description = "유저가 회원 탈퇴를 진행합니다")
    @DeleteMapping("/users")
    fun deleteUser(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
    ): ResponseEntity<UserDeleteResponse> {
        userService.deleteUser(userId)
        return ResponseEntity.ok(UserDeleteResponse(true, userId))
    }
}
