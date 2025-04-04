package uket.uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uket.uket.api.admin.response.GetAllOrganizationResponse

@Tag(name = "소속 관련 API", description = "소속 관련 API 입니다.")
@RestController
@RequestMapping("/admin/organizations")
@ApiResponse(responseCode = "200", description = "단체")
interface OrganizationApi {
    @Operation(summary = "모든 단체 조회 API", description = "모든 단 목록을 조회합니다.")
    @GetMapping("")
    fun getAllOrganizations(): ResponseEntity<GetAllOrganizationResponse>
}
