package uket.uket.api.admin

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import uket.domain.admin.service.AdminService

@Controller
class UsersController(
    val adminService: AdminService,
) {
    @GetMapping("/admin/users")
    fun getAllUsers(): ResponseEntity<GetAllUsersResponse> {
        val users = adminService.findAll()
        return ResponseEntity.ok(GetAllUsersResponse(users))
    }

    @DeleteMapping("/admin/users/{adminId}")
    fun deleteUser(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteUserResponse> {
        val user = adminService.findById(adminId)
        adminService.deleteAdmin(adminId)
        return ResponseEntity.ok(DeleteUserResponse(adminId, user.name))
    }
}
