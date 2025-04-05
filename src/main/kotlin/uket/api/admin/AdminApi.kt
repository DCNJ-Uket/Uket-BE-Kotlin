package uket.uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.uket.api.admin.request.EmailLoginRequest
import uket.uket.api.admin.request.RegisterAdminPasswordCommand
import uket.uket.api.admin.response.RegisterAdminResponse
import uket.uket.api.admin.response.SendEmailResponse
import uket.uket.auth.dto.AdminAuthToken

@Tag(name = "어드민 멤버 관련 API", description = "어드민 멤버 관련 API 입니다.")
@RestController
@RequestMapping("/admin/users")
@ApiResponse(responseCode = "200", description = "OK")
interface AdminApi {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 등록 메일 발송", description = "비밀번호를 제외한 어드민 멤버를 등록 후 회원가입 메일을 발송합니다.")
    @PostMapping("/register")
    fun sendInviteEmail(
        @RequestBody registerAdminWithoutPasswordCommand: RegisterAdminWithoutPasswordCommand
    ): ResponseEntity<SendEmailResponse>

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 회원가입", description = "비밀번호를 받아 어드민 멤버 회원가입을 진행합니다.")
    @PostMapping("/password")
    fun registerPassword(
        @RequestBody registerAdminPasswordCommand: RegisterAdminPasswordCommand
    ): ResponseEntity<RegisterAdminResponse>

    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 진행합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: EmailLoginRequest
    ): ResponseEntity<AdminAuthToken>
}
