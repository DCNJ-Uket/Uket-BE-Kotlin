package uket.uket.api.admin.impl

import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import uket.domain.admin.service.AdminService
import uket.uket.api.admin.AdminApi
import uket.uket.api.admin.dto.AdminDto
import uket.uket.api.admin.response.DeleteAdminResponse
import uket.uket.api.admin.response.GetAdminsResponse

@Controller
class AdminController(
    val adminService: AdminService,
) : AdminApi {
    override fun getAdmins(page: Int, size: Int): ResponseEntity<GetAdminsResponse> {
        val pageRequest = PageRequest.of(page - 1, size)
        val adminPage = adminService.findAdminsByPage(pageRequest)
        val adminDtos = adminPage.content
            .stream()
            .map { AdminDto.from(it) }
            .toList()
        return ResponseEntity.ok(
            GetAdminsResponse(
                adminDtos,
                adminPage.number,
                adminPage.numberOfElements,
                adminPage.isFirst,
                adminPage.isLast,
                adminPage.totalElements,
                adminPage.totalPages,
                adminPage.isEmpty,
            ),
        )
    }

    override fun deleteAdmin(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteAdminResponse> {
        val user = adminService.findById(adminId)
        adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(DeleteAdminResponse(adminId, user.name))
    }
}
