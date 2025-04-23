package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.response.GetOrganizationDropdownItemsResponse
import uket.domain.admin.service.OrganizationService

@Tag(name = "단체 관련 API", description = "단체 관련 API 입니다.")
@RestController
@ApiResponse(responseCode = "200", description = "단체")
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "모든 등록 가능한 단체 조회 API", description = "모든 등록 가능한 단체 목록을 조회합니다.")
    @GetMapping("/admin/organizations")
    fun getAllOrganizations(): ResponseEntity<GetOrganizationDropdownItemsResponse> {
        val dropdownItems = organizationService.findAllAvailableIdAndNames()
        return ResponseEntity.ok(GetOrganizationDropdownItemsResponse(dropdownItems))
    }
}
