package uket.uket.api.admin.impl

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import uket.domain.admin.service.AdminService
import uket.uket.api.admin.AdminApi
import uket.uket.api.admin.dto.AdminDto
import uket.uket.api.admin.response.DeleteUserResponse
import uket.uket.api.admin.response.GetAllUsersResponse

@Controller
class AdminController(
    val adminService: AdminService,
) : AdminApi {
    override fun getAllAdmins(): ResponseEntity<GetAllUsersResponse> {
        val admins = adminService.findAllAdminsWithOrganizationId()
        val adminDtos = admins.stream().map { AdminDto.from(it) }.toList()
        return ResponseEntity.ok(GetAllUsersResponse(adminDtos))
    }

    override fun deleteAdmin(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteUserResponse> {
        val user = adminService.findById(adminId)
        adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(DeleteUserResponse(adminId, user.name))
    }
}
