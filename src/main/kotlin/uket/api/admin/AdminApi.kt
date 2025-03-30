package uket.uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.uket.api.admin.response.DeleteAdminResponse
import uket.uket.api.admin.response.GetAdminsResponse

@Tag(name = "어드민 멤버 관련 API", description = "어드민 멤버 관련 API 입니다.")
@RestController
@RequestMapping("/admins")
@ApiResponse(responseCode = "200", description = "OK")
interface AdminApi {
    @Operation(summary = "어드민 멤버 목록 조회", description = "어드민 목록을 페이지로 조회합니다.")
    @GetMapping("")
    fun getAdmins(
        @RequestParam page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<GetAdminsResponse>

    @Operation(summary = "어드민 멤버 삭제", description = "지정한 어드민 멤버를 삭제합니다.")
    @DeleteMapping("/{adminId}")
    fun deleteAdmin(
        @PathVariable("adminId") adminId: Long,
    ): ResponseEntity<DeleteAdminResponse>
}
