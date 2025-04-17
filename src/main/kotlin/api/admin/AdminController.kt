package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import uket.api.admin.request.EmailLoginRequest
import uket.api.admin.request.RegisterAdminPasswordRequest
import uket.api.admin.request.SendEmailRequest
import uket.api.admin.response.AdminInfoResponse
import uket.api.admin.response.RegisterAdminResponse
import uket.api.admin.response.SendEmailResponse
import uket.auth.config.adminId.LoginAdminId
import uket.auth.dto.AdminAuthToken
import uket.domain.admin.dto.AdminWithOrganizationDto
import uket.domain.admin.service.AdminService
import uket.facade.AdminAuthEmailFacade

@Tag(name = "어드민 멤버 관련 API", description = "어드민 멤버 관련 API 입니다.")
@RestController
@RequestMapping("/admin/users")
@ApiResponse(responseCode = "200", description = "OK")
class AdminController(
    private val adminAuthEmailFacade: AdminAuthEmailFacade,
    private val adminService: AdminService,
) {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 등록 메일 발송", description = "비밀번호를 제외한 어드민 멤버를 등록 후 회원가입 메일을 발송합니다.")
    @PostMapping("/register")
    fun sendInviteEmail(
        @RequestBody sendEmailRequest: SendEmailRequest,
    ): ResponseEntity<SendEmailResponse> {
        val response: SendEmailResponse = adminAuthEmailFacade.sendAuthEmail(sendEmailRequest)
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 회원가입", description = "비밀번호를 받아 어드민 멤버 회원가입을 진행합니다.")
    @PostMapping("/password")
    fun registerPassword(
        @RequestBody request: RegisterAdminPasswordRequest,
    ): ResponseEntity<RegisterAdminResponse> {
        val authentication = SecurityContextHolder.getContext().authentication
        val token = authentication.credentials as String
        val response: RegisterAdminResponse = adminAuthEmailFacade.registerAdminWithPassword(token, request.email, request.password)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "어드민 로그인", description = "어드민 로그인을 진행합니다.")
    @PostMapping("/login")
    fun login(
        @RequestBody request: EmailLoginRequest,
    ): ResponseEntity<AdminAuthToken> {
        val adminAuthToken: AdminAuthToken = adminAuthEmailFacade.login(request.email, request.password)
        return ResponseEntity.ok(adminAuthToken)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 본인 정보 조회", description = "로그인한 어드민 유저의 정보를 조회합니다.")
    @GetMapping("/info")
    fun getAdminInfo(
        @Parameter(hidden = true)
        @LoginAdminId
        adminId: Long,
    ) : ResponseEntity<AdminInfoResponse> {
        val adminInfo: AdminWithOrganizationDto = adminService.getAdminInfo(adminId)
        val response = AdminInfoResponse.from(adminInfo)
        return ResponseEntity.ok(response)
    }
}
