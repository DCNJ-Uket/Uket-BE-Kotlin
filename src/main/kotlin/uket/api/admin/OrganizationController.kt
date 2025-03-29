package uket.uket.api.admin

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import uket.domain.admin.service.OrganizationService

@Controller
class OrganizationController(
    private val organizationService: OrganizationService,
) {
    @GetMapping("/organizations")
    fun getAllOrganizations(): ResponseEntity<GetAllOrganizationResponse> {
        val organizations = organizationService.findAll()
        return ResponseEntity.ok(GetAllOrganizationResponse(organizations))
    }
}
