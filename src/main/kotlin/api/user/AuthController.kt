package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.LoginRequest
import uket.api.user.response.AuthResponse
import uket.auth.jwt.JwtAuthTokenUtil
import uket.domain.user.enums.Platform
import uket.domain.user.service.AuthService
import uket.domain.user.service.UserService

@RestController
class AuthController(
    private val authService: AuthService,
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
    private val userService: UserService,
) {
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 진행합니다.")
    @PostMapping("/login/{provider}")
    fun login(
        @RequestBody request: LoginRequest,
        @PathVariable provider: String,
    ): ResponseEntity<AuthResponse> {
        val platform = Platform.fromString(provider)
        val authToken = authService.login(platform, request.redirectUri, request.code)

        val userId = jwtAuthTokenUtil.getId(authToken.accessToken)
        val loginUser = userService.getById(userId)

        val response = AuthResponse.of(loginUser, authToken)
        return ResponseEntity.ok(response)
    }
}
