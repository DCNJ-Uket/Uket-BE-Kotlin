package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.request.EmailLoginRequest
import uket.api.admin.request.RegisterAdminPasswordRequest
import uket.api.admin.request.SendEmailRequest
import uket.api.admin.response.AdminInfoResponse
import uket.api.admin.response.CheckRegisterExpiredResponse
import uket.api.admin.response.DeleteAdminResponse
import uket.api.admin.response.RegisterAdminResponse
import uket.api.admin.response.SendEmailResponse
import uket.auth.config.adminId.LoginAdminId
import uket.auth.dto.AdminAuthToken
import uket.common.response.CustomPageResponse
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

    @Operation(summary = "회원가입 링크 만료 여부 확인", description = "회원가입 링크의 토큰을 통해 링크 만료 여부를 확인합니다.")
    @GetMapping("/register-expired")
    fun checkTokenExpired(
        @RequestParam("token") token: String,
    ): ResponseEntity<CheckRegisterExpiredResponse> {
        val isExpired = adminAuthEmailFacade.checkEmailTokenExpired(token)
        return ResponseEntity.ok(CheckRegisterExpiredResponse(isExpired))
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
    @Operation(summary = "어드민 본인 정보 상세 조회", description = "로그인한 어드민 유저의 정보를 조회합니다.")
    @GetMapping("/info")
    fun getAdminInfo(
        @Parameter(hidden = true)
        @LoginAdminId
        adminId: Long,
    ): ResponseEntity<AdminInfoResponse> {
        val adminInfo: AdminWithOrganizationDto = adminService.getAdminInfo(adminId)
        val response = AdminInfoResponse.from(adminInfo)
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 목록 조회", description = "어드민 목록을 페이지로 조회합니다.")
    @GetMapping
    fun getAdmins(page: Int, size: Int): ResponseEntity<CustomPageResponse<AdminWithOrganizationDto>> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val adminPage = adminService.findAdminsWithOrganizationIdAndNameByPage(pageRequest)
        return ResponseEntity.ok(CustomPageResponse(adminPage))
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 멤버 삭제", description = "지정한 어드민 멤버를 삭제합니다.")
    @DeleteMapping("/{adminId}")
    fun deleteAdmin(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteAdminResponse> {
        val user = adminService.getById(adminId)
        adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(DeleteAdminResponse(adminId, user.name))
    }
}
