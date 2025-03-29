package uket.uket.api.admin

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
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
}
