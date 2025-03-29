package uket.uket.api.admin.impl

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import uket.domain.admin.service.OrganizationService
import uket.uket.api.admin.OrganizationApi
import uket.uket.api.admin.response.GetAllOrganizationResponse

@Controller
class OrganizationController(
    private val organizationService: OrganizationService,
) : OrganizationApi {
    override fun getAllOrganizations(): ResponseEntity<GetAllOrganizationResponse> {
        val organizations = organizationService.findAll()
        return ResponseEntity.ok(GetAllOrganizationResponse(organizations))
    }
}
