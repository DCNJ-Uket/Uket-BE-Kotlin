package uket.uket.api.admin

import uket.domain.admin.entity.Organization

data class GetAllOrganizationResponse(
    val organizations: List<Organization>,
)
