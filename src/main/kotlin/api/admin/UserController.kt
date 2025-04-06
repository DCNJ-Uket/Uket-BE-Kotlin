package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.response.DeleteAdminResponse
import uket.common.response.CustomPageResponse
import uket.domain.admin.dto.AdminWithOrganizationIdDto
import uket.domain.admin.service.AdminService

@Tag(name = "어드민 멤버 관련 API", description = "어드민 멤버 관련 API 입니다.")
@RestController
@ApiResponse(responseCode = "200", description = "OK")
class UserController(
    val adminService: AdminService,
) {
    @Operation(summary = "어드민 멤버 목록 조회", description = "어드민 목록을 페이지로 조회합니다.")
    @GetMapping("/admin/users")
    fun getAdmins(page: Int, size: Int): ResponseEntity<CustomPageResponse<AdminWithOrganizationIdDto>> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val adminPage = adminService.findAdminsWithOrganizationIdByPage(pageRequest)
        return ResponseEntity.ok(CustomPageResponse.from(adminPage))
    }

    @Operation(summary = "어드민 멤버 삭제", description = "지정한 어드민 멤버를 삭제합니다.")
    @DeleteMapping("/admin/users/{adminId}")
    fun deleteAdmin(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteAdminResponse> {
        val user = adminService.getById(adminId)
        adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(DeleteAdminResponse(adminId, user.name))
    }
}
